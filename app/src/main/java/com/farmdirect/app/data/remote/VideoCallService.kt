package com.farmdirect.app.data.remote

import com.farmdirect.app.domain.model.CallSignal
import com.farmdirect.app.domain.model.VideoCall
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class VideoCallService {
    
    private val _currentCall = MutableStateFlow<VideoCall?>(null)
    val currentCall = _currentCall.asStateFlow()
    
    private val _incomingCall = MutableStateFlow<VideoCall?>(null)
    val incomingCall = _incomingCall.asStateFlow()
    
    /**
     * Initiate a video call
     */
    fun startCall(callerId: String, callerName: String, receiverId: String, receiverName: String, callType: String = "VIDEO"): VideoCall {
        val call = VideoCall(
            callId = java.util.UUID.randomUUID().toString(),
            callerId = callerId,
            callerName = callerName,
            receiverId = receiverId,
            receiverName = receiverName,
            callType = if (callType == "VIDEO") com.farmdirect.app.domain.model.CallType.VIDEO else com.farmdirect.app.domain.model.CallType.VOICE,
            status = com.farmdirect.app.domain.model.CallStatus.RINGING,
            roomId = java.util.UUID.randomUUID().toString()
        )
        _currentCall.value = call
        
        // Send signaling message via WebSocket/API
        sendSignal(CallSignal(
            type = com.farmdirect.app.domain.model.SignalType.OFFER,
            callId = call.callId,
            senderId = callerId,
            receiverId = receiverId
        ))
        
        return call
    }
    
    /**
     * Accept incoming call
     */
    fun acceptCall(callId: String): VideoCall? {
        val call = _incomingCall.value
        if (call != null && call.callId == callId) {
            val accepted = call.copy(status = com.farmdirect.app.domain.model.CallStatus.CONNECTING)
            _currentCall.value = accepted
            _incomingCall.value = null
            
            sendSignal(CallSignal(
                type = com.farmdirect.app.domain.model.SignalType.ANSWER,
                callId = callId,
                senderId = call.receiverId,
                receiverId = call.callerId
            ))
            return accepted
        }
        return null
    }
    
    /**
     * Reject incoming call
     */
    fun rejectCall(callId: String) {
        val call = _incomingCall.value
        if (call != null && call.callId == callId) {
            sendSignal(CallSignal(
                type = com.farmdirect.app.domain.model.SignalType.REJECT,
                callId = callId,
                senderId = call.receiverId,
                receiverId = call.callerId
            ))
            _incomingCall.value = null
        }
    }
    
    /**
     * End current call
     */
    fun endCall() {
        _currentCall.value?.let { call ->
            sendSignal(CallSignal(
                type = com.farmdirect.app.domain.model.SignalType.HANGUP,
                callId = call.callId,
                senderId = call.callerId,
                receiverId = call.receiverId
            ))
            _currentCall.value = call.copy(
                status = com.farmdirect.app.domain.model.CallStatus.ENDED,
                endedAt = System.currentTimeMillis(),
                duration = System.currentTimeMillis() - call.startedAt
            )
        }
    }
    
    /**
     * Handle incoming signaling message
     */
    fun handleIncomingSignal(signal: CallSignal) {
        when (signal.type) {
            com.farmdirect.app.domain.model.SignalType.OFFER -> {
                // Incoming call
                _incomingCall.value = VideoCall(
                    callId = signal.callId,
                    callerId = signal.senderId,
                    receiverId = signal.receiverId,
                    status = com.farmdirect.app.domain.model.CallStatus.RINGING
                )
            }
            com.farmdirect.app.domain.model.SignalType.HANGUP -> {
                _currentCall.value = null
            }
            com.farmdirect.app.domain.model.SignalType.REJECT -> {
                _currentCall.value = _currentCall.value?.copy(status = com.farmdirect.app.domain.model.CallStatus.REJECTED)
            }
            else -> {}
        }
    }
    
    private fun sendSignal(signal: CallSignal) {
        // In production, send via WebSocket or Firebase Realtime Database
        // This would notify the other user's device
    }
}
