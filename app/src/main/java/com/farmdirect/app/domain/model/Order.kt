package com.farmdirect.app.domain.model

data class Order(
    val id: String = "",
    val orderNumber: String = "",
    val cropId: String = "",
    val cropTitle: String = "",
    val buyerId: String = "",
    val buyerName: String = "",
    val sellerId: String = "",
    val sellerName: String = "",
    val quantity: Double = 0.0,
    val unit: String = "kg",
    val pricePerUnit: Double = 0.0,
    val subtotal: Double = 0.0,
    val platformFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING",
    val paymentStatus: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
)

data class PlaceOrderRequest(val cropId: String, val quantity: Double)
