package com.farmdirect.app.util

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.io.File

class AchievementSystem(private val context: Context) {
    
    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements = _achievements.asStateFlow()
    
    private val _recentlyUnlocked = MutableStateFlow<Achievement?>(null)
    val recentlyUnlocked = _recentlyUnlocked.asStateFlow()
    
    private val dataFile: File
        get() = File(context.filesDir, "achievements.json")
    
    data class Achievement(
        val id: String,
        val name: String,
        val description: String,
        val emoji: String,
        val category: AchievementCategory,
        val progress: Int = 0,
        val target: Int = 1,
        val isUnlocked: Boolean = false,
        val unlockedAt: Long? = null,
        val reward: String = "",
        val rarity: AchievementRarity = AchievementRarity.COMMON
    )
    
    enum class AchievementCategory {
        SELLING, BUYING, COMMUNITY, LEARNING, CONSISTENCY, MILESTONE
    }
    
    enum class AchievementRarity {
        COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
    }
    
    private val allAchievements = listOf(
        Achievement("first_sale", "First Harvest", "Complete your first sale", "🌱", AchievementCategory.SELLING, target = 1, reward = "KES 50 bonus", rarity = AchievementRarity.COMMON),
        Achievement("ten_sales", "Market Regular", "Complete 10 sales", "🌿", AchievementCategory.SELLING, target = 10, reward = "Featured listing", rarity = AchievementRarity.UNCOMMON),
        Achievement("fifty_sales", "Master Farmer", "Complete 50 sales", "🌾", AchievementCategory.SELLING, target = 50, reward = "Premium badge", rarity = AchievementRarity.RARE),
        Achievement("hundred_sales", "Farm Legend", "Complete 100 sales", "👑", AchievementCategory.SELLING, target = 100, reward = "1 month free Premium", rarity = AchievementRarity.EPIC),
        Achievement("thousand_sales", "Agricultural Icon", "Complete 1,000 sales", "🏆", AchievementCategory.SELLING, target = 1000, reward = "Lifetime Premium", rarity = AchievementRarity.LEGENDARY),
        Achievement("first_buy", "Smart Shopper", "Complete your first purchase", "🛒", AchievementCategory.BUYING, target = 1, reward = "KES 50 bonus", rarity = AchievementRarity.COMMON),
        Achievement("five_star", "Perfect Rating", "Get a 5-star rating", "⭐", AchievementCategory.CONSISTENCY, target = 1, reward = "Trust badge", rarity = AchievementRarity.UNCOMMON),
        Achievement("fast_responder", "Quick Responder", "Respond to messages within 10 minutes", "⚡", AchievementCategory.CONSISTENCY, target = 5, reward = "Fast Responder badge", rarity = AchievementRarity.RARE),
        Achievement("daily_login_7", "Weekly Streak", "Login 7 days in a row", "🔥", AchievementCategory.CONSISTENCY, target = 7, reward = "KES 20 bonus", rarity = AchievementRarity.UNCOMMON),
        Achievement("daily_login_30", "Monthly Dedication", "Login 30 days in a row", "💪", AchievementCategory.CONSISTENCY, target = 30, reward = "KES 100 bonus", rarity = AchievementRarity.RARE),
        Achievement("refer_friend", "Networker", "Refer a friend who joins", "🤝", AchievementCategory.COMMUNITY, target = 1, reward = "KES 100 bonus", rarity = AchievementRarity.COMMON),
        Achievement("refer_10", "Community Builder", "Refer 10 friends who join", "🏘️", AchievementCategory.COMMUNITY, target = 10, reward = "KES 500 bonus", rarity = AchievementRarity.RARE),
        Achievement("complete_course", "Lifelong Learner", "Complete a farming course", "🎓", AchievementCategory.LEARNING, target = 1, reward = "Certificate", rarity = AchievementRarity.UNCOMMON),
        Achievement("help_others", "Community Helper", "Answer 10 questions in forums", "🦸", AchievementCategory.COMMUNITY, target = 10, reward = "Helper badge", rarity = AchievementRarity.RARE),
        Achievement("video_creator", "Content Creator", "Post 5 farming videos", "🎬", AchievementCategory.COMMUNITY, target = 5, reward = "Creator badge", rarity = AchievementRarity.UNCOMMON)
    )
    
    init {
        loadProgress()
    }
    
    fun updateProgress(achievementId: String, progress: Int) {
        val updated = _achievements.value.map { achievement ->
            if (achievement.id == achievementId && !achievement.isUnlocked) {
                val newProgress = achievement.progress + progress
                val isNowUnlocked = newProgress >= achievement.target
                
                if (isNowUnlocked) {
                    val unlocked = achievement.copy(
                        progress = newProgress,
                        isUnlocked = true,
                        unlockedAt = System.currentTimeMillis()
                    )
                    _recentlyUnlocked.value = unlocked
                    return@map unlocked
                } else {
                    return@map achievement.copy(progress = newProgress)
                }
            }
            achievement
        }
        _achievements.value = updated
        saveProgress()
    }
    
    fun getUnlockedCount(): Int = _achievements.value.count { it.isUnlocked }
    fun getTotalCount(): Int = allAchievements.size
    fun getProgressPercentage(): Float = if (allAchievements.isNotEmpty()) getUnlockedCount().toFloat() / getTotalCount() else 0f
    
    private fun loadProgress() {
        try {
            if (dataFile.exists()) {
                val json = JSONObject(dataFile.readText())
                _achievements.value = allAchievements.map { achievement ->
                    val saved = json.optJSONObject(achievement.id)
                    if (saved != null) {
                        achievement.copy(
                            progress = saved.optInt("progress", 0),
                            isUnlocked = saved.optBoolean("isUnlocked", false),
                            unlockedAt = saved.optLong("unlockedAt", 0).takeIf { it > 0 }
                        )
                    } else achievement
                }
            } else {
                _achievements.value = allAchievements
            }
        } catch (e: Exception) {
            _achievements.value = allAchievements
        }
    }
    
    private fun saveProgress() {
        try {
            val json = JSONObject()
            _achievements.value.forEach { achievement ->
                json.put(achievement.id, JSONObject().apply {
                    put("progress", achievement.progress)
                    put("isUnlocked", achievement.isUnlocked)
                    put("unlockedAt", achievement.unlockedAt ?: 0)
                })
            }
            dataFile.writeText(json.toString())
        } catch (e: Exception) { }
    }
}
