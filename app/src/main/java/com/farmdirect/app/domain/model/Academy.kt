package com.farmdirect.app.domain.model

data class Course(
    val id: String = "", val title: String = "", val description: String = "",
    val category: String = "CROP_FARMING", val instructor: String = "",
    val rating: Double = 0.0, val enrolledCount: Int = 0, val totalLessons: Int = 0,
    val isEnrolled: Boolean = false, val isCompleted: Boolean = false,
    val thumbnailUrl: String = ""
)

data class Certificate(val userId: String = "", val courseTitle: String = "", val certificateUrl: String = "", val issuedAt: Long = System.currentTimeMillis())
