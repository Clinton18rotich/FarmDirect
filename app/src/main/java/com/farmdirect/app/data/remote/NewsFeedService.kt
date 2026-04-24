package com.farmdirect.app.data.remote

import com.farmdirect.app.domain.model.NewsArticle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsFeedService {
    
    private val _articles = MutableStateFlow<List<NewsArticle>>(emptyList())
    val articles = _articles.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    fun loadNews() {
        _isLoading.value = true
        _articles.value = sampleNews
        _isLoading.value = false
    }
    
    fun searchNews(query: String): List<NewsArticle> {
        return _articles.value.filter {
            it.title.contains(query, true) || it.content.contains(query, true)
        }
    }
}

data class NewsArticle(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val summary: String = "",
    val source: String = "",
    val author: String = "",
    val category: NewsCategory = NewsCategory.GENERAL,
    val imageUrl: String = "",
    val publishedAt: Long = System.currentTimeMillis(),
    val readTime: Int = 3,
    val isSaved: Boolean = false
)

enum class NewsCategory {
    GENERAL, PRICES, WEATHER, TECHNOLOGY, POLICY, MARKET, SUCCESS_STORIES
}

val sampleNews = listOf(
    NewsArticle("1", "Maize Prices Rise in Western Kenya", "Maize prices have increased by 15% in the past week due to high demand from millers...", source = "FarmDirect News", category = NewsCategory.PRICES),
    NewsArticle("2", "Government Launches Fertilizer Subsidy Program", "The Ministry of Agriculture has announced a new subsidy program that will reduce fertilizer costs by 40%...", source = "Ministry of Agriculture", category = NewsCategory.POLICY),
    NewsArticle("3", "New Drought-Resistant Maize Variety Released", "KALRO has released a new maize variety that requires 30% less water...", source = "KALRO", category = NewsCategory.TECHNOLOGY),
    NewsArticle("4", "Export Market Opens for Kenyan Avocados", "Kenya has secured a new export deal with China for fresh avocados...", source = "Business Daily", category = NewsCategory.MARKET),
    NewsArticle("5", "Young Farmer Makes Millions from Tomato Farming", "24-year-old Peter Mwangi from Kitale shares his success story...", source = "FarmDirect Success Stories", category = NewsCategory.SUCCESS_STORIES)
)
