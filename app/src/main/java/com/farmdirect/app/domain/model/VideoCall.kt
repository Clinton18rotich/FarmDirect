package com.farmdirect.app.domain.model

data class VideoCall(
    val callId: String = "",
    val callerId: String = "",
    val callerName: String = "",
    val callerImage: String = "",
    val receiverId: String = "",
    val receiverName: String = "",
    val receiverImage: String = "",
    val callType: CallType = CallType.VIDEO,
    val status: CallStatus = CallStatus.RINGING,
    val roomId: String = "",
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long? = null,
    val duration: Long = 0,
    val isMuted: Boolean = false,
    val isCameraOff: Boolean = false,
    val isSpeakerOn: Boolean = true
)

enum class CallType {
    VIDEO, VOICE
}

enum class CallStatus {
    RINGING,        // Calling the receiver
    CONNECTING,     // Establishing connection
    CONNECTED,      // Call active
    RECONNECTING,   // Lost connection, trying again
    ENDED,          // Call ended
    MISSED,         // Receiver didn't answer
    REJECTED,       // Receiver declined
    BUSY            // Receiver on another call
}

data class CallSignal(
    val type: SignalType,
    val callId: String,
    val senderId: String,
    val receiverId: String,
    val data: Map<String, String> = emptyMap()
)

enum class SignalType {
    OFFER,           // Call invitation
    ANSWER,          // Call accepted
    ICE_CANDIDATE,   // Network path found
    HANGUP,          // Call ended
    REJECT,          // Call declined
    BUSY             // Receiver busy
}

data class IceServer(
    val url: String,
    val username: String = "",
    val credential: String = ""
)

data class CallHistory(
    val id: String = "",
    val otherUserId: String = "",
    val otherUserName: String = "",
    val otherUserImage: String = "",
    val callType: CallType = CallType.VIDEO,
    val direction: CallDirection = CallDirection.OUTGOING,
    val status: CallStatus = CallStatus.ENDED,
    val duration: Long = 0,
    val timestamp: Long = System.currentTimeMillis()
)

enum class CallDirection {
    INCOMING, OUTGOING
}
