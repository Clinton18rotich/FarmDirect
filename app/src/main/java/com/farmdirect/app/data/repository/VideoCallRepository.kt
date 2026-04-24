package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.VideoCallService
import com.farmdirect.app.domain.model.CallHistory
import com.farmdirect.app.domain.model.VideoCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoCallRepository @Inject constructor(
    private val videoCallService: VideoCallService
) {
    
    val currentCall = videoCallService.currentCall
    val incomingCall = videoCallService.incomingCall
    
    suspend fun startCall(callerId: String, callerName: String, receiverId: String, receiverName: String, callType: String): VideoCall {
        return videoCallService.startCall(callerId, callerName, receiverId, receiverName, callType)
    }
    
    suspend fun acceptCall(callId: String): VideoCall? {
        return videoCallService.acceptCall(callId)
    }
    
    suspend fun rejectCall(callId: String) {
        videoCallService.rejectCall(callId)
    }
    
    suspend fun endCall() {
        videoCallService.endCall()
    }
    
    suspend fun getCallHistory(userId: String): List<CallHistory> {
        // Mock call history
        return listOf(
            CallHistory(
                id = "1", otherUserId = "user2", otherUserName = "Mary's Farm",
                callType = com.farmdirect.app.domain.model.CallType.VIDEO,
                direction = com.farmdirect.app.domain.model.CallDirection.INCOMING,
                duration = 180, timestamp = System.currentTimeMillis() - 3600000
            ),
            CallHistory(
                id = "2", otherUserId = "user3", otherUserName = "Peter Mwangi",
                callType = com.farmdirect.app.domain.model.CallType.VOICE,
                direction = com.farmdirect.app.domain.model.CallDirection.OUTGOING,
                duration = 45, timestamp = System.currentTimeMillis() - 7200000
            )
        )
    }
}
