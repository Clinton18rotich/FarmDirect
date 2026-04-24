package com.farmdirect.app.data.repository

import com.farmdirect.app.data.remote.RetrofitClient
import com.farmdirect.app.domain.model.Certificate
import com.farmdirect.app.domain.model.Course
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcademyRepository @Inject constructor() {
    suspend fun getCourses(token: String, category: String? = null): List<Course> {
        val response = RetrofitClient.apiService.getCourses("Bearer $token", category)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
    suspend fun enrollCourse(token: String, courseId: String): Boolean {
        return RetrofitClient.apiService.enrollCourse("Bearer $token", mapOf("courseId" to courseId)).isSuccessful
    }
    suspend fun getCertificate(token: String, courseId: String): Certificate? {
        val response = RetrofitClient.apiService.getCertificate("Bearer $token", courseId)
        return if (response.isSuccessful) response.body() else null
    }
}
