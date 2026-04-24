package com.farmdirect.app.domain.model

data class Cooperative(
    val id: String = "", val name: String = "", val description: String = "",
    val location: String = "", val memberCount: Int = 0, val totalRevenue: Double = 0.0,
    val rating: Double = 0.0, val isMember: Boolean = false
)

data class CreateCooperativeRequest(val name: String, val description: String, val location: String)
