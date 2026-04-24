package com.farmdirect.app.ui.screens.academy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farmdirect.app.data.local.FarmDirectPreferences
import com.farmdirect.app.data.repository.AcademyRepository
import com.farmdirect.app.domain.model.Course
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AcademyViewModel @Inject constructor(private val preferences: FarmDirectPreferences, private val repository: AcademyRepository) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses = _courses.asStateFlow()

    fun loadCourses(category: String? = null) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; _courses.value = repository.getCourses(token, category) }
    }

    fun enroll(courseId: String) {
        viewModelScope.launch { val token = preferences.authToken.firstOrNull() ?: ""; repository.enrollCourse(token, courseId) }
    }
}
