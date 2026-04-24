package com.farmdirect.app.domain.model

data class Dispute(
    val id: String = "", val caseNumber: String = "", val orderId: String = "",
    val initiatorName: String = "", val respondentName: String = "",
    val type: String = "WRONG_QUALITY", val description: String = "",
    val status: String = "OPEN", val resolution: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class OpenDisputeRequest(val orderId: String, val type: String, val reason: String, val description: String, val desiredOutcome: String)
