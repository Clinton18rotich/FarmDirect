package com.farmdirect.app.domain.model

data class Payment(
    val id: String = "",
    val orderId: String = "",
    val amount: Double = 0.0,
    val fee: Double = 0.0,
    val total: Double = 0.0,
    val method: String = "MPESA",
    val status: String = "PENDING",
    val mpesaReceiptNumber: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

data class MpesaPaymentRequest(
    val phoneNumber: String, val amount: Double,
    val accountReference: String, val transactionDescription: String
)
