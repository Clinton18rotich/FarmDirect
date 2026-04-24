package com.farmdirect.app.ui.screens.call

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.repository.VideoCallRepository
import com.farmdirect.app.domain.model.CallHistory
import com.farmdirect.app.domain.model.VideoCall
import com.farmdirect.app.domain.webrtc.WebRTCManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CallUiState(
    val call: VideoCall? = null,
    val incomingCall: VideoCall? = null,
    val callHistory: List<CallHistory> = emptyList(),
    val isCallActive: Boolean = false,
    val isIncomingCall: Boolean = false,
    val isMuted: Boolean = false,
    val isCameraOff: Boolean = false,
    val isSpeakerOn: Boolean = true,
    val callDuration: Long = 0,
    val callState: String = "DISCONNECTED"
)

@HiltViewModel
class CallViewModel @Inject constructor(
    private val repository: VideoCallRepository,
    private val webRTCManager: WebRTCManager
) : ViewModel() {
    
    private val _state = MutableStateFlow(CallUiState())
    val state = _state.asStateFlow()
    
    private var durationTimer: kotlinx.coroutines.Job? = null
    
    init {
        // Observe incoming calls
        viewModelScope.launch {
            repository.incomingCall.collect { call ->
                if (call != null) {
                    _state.value = _state.value.copy(
                        incomingCall = call,
                        isIncomingCall = true
                    )
                }
            }
        }
        
        // Observe current call
        viewModelScope.launch {
            repository.currentCall.collect { call ->
                if (call != null) {
                    _state.value = _state.value.copy(
                        call = call,
                        isCallActive = call.status == com.farmdirect.app.domain.model.CallStatus.CONNECTED
                    )
                } else {
                    _state.value = _state.value.copy(
                        call = null,
                        isCallActive = false,
                        callDuration = 0
                    )
                    stopDurationTimer()
                }
            }
        }
    }
    
    /**
     * Start a video call
     */
    fun startCall(receiverId: String, receiverName: String, callType: String = "VIDEO") {
        viewModelScope.launch {
            val call = repository.startCall(
                callerId = "current_user",
                callerName = "You",
                receiverId = receiverId,
                receiverName = receiverName,
                callType = callType
            )
            webRTCManager.initialize(call.callId, true)
            startDurationTimer()
        }
    }
    
    /**
     * Accept incoming call
     */
    fun acceptCall() {
        viewModelScope.launch {
            _state.value.incomingCall?.let { call ->
                repository.acceptCall(call.callId)
                webRTCManager.initialize(call.callId, false)
                _state.value = _state.value.copy(isIncomingCall = false)
                startDurationTimer()
            }
        }
    }
    
    /**
     * Reject incoming call
     */
    fun rejectCall() {
        viewModelScope.launch {
            _state.value.incomingCall?.let { call ->
                repository.rejectCall(call.callId)
                _state.value = _state.value.copy(
                    incomingCall = null,
                    isIncomingCall = false
                )
            }
        }
    }
    
    /**
     * End current call
     */
    fun endCall() {
        viewModelScope.launch {
            repository.endCall()
            webRTCManager.endCall()
            stopDurationTimer()
        }
    }
    
    /**
     * Toggle mute
     */
    fun toggleMute() {
        _state.value = _state.value.copy(isMuted = webRTCManager.toggleMute())
    }
    
    /**
     * Toggle camera
     */
    fun toggleCamera() {
        _state.value = _state.value.copy(isCameraOff = webRTCManager.toggleCamera())
    }
    
    /**
     * Toggle speaker
     */
    fun toggleSpeaker() {
        _state.value = _state.value.copy(isSpeakerOn = webRTCManager.toggleSpeaker())
    }
    
    /**
     * Switch camera
     */
    fun switchCamera() {
        webRTCManager.switchCamera()
    }
    
    /**
     * Load call history
     */
    fun loadCallHistory(userId: String) {
        viewModelScope.launch {
            val history = repository.getCallHistory(userId)
            _state.value = _state.value.copy(callHistory = history)
        }
    }
    
    private fun startDurationTimer() {
        durationTimer?.cancel()
        durationTimer = viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1000)
                _state.value = _state.value.copy(
                    callDuration = _state.value.callDuration + 1
                )
            }
        }
    }
    
    private fun stopDurationTimer() {
        durationTimer?.cancel()
        durationTimer = null
    }
    
    override fun onCleared() {
        super.onCleared()
        stopDurationTimer()
        webRTCManager.endCall()
    }
}
