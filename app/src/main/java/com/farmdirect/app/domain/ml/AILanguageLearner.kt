package com.farmdirect.app.domain.ml

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.math.max

class AILanguageLearner(private val context: Context) {
    
    private val _allLanguages = MutableStateFlow<List<LanguageProfile>>(emptyList())
    val allLanguages = _allLanguages.asStateFlow()
    
    private val _learningSessions = MutableStateFlow<LearningSession?>(null)
    val learningSession = _learningSessions.asStateFlow()
    
    private val dataFile: File
        get() = File(context.filesDir, "ai_languages.json")
    
    // ═══════════════════════════════════════
    // COMPLETE LANGUAGE DATABASE
    // ═══════════════════════════════════════
    
    data class LanguageProfile(
        val code: String,
        val name: String,
        val nativeName: String,
        val flag: String,
        val region: String,
        val speakers: String,
        val dialects: List<String>,
        val phrases: MutableMap<String, String> = mutableMapOf(),
        val grammarRules: MutableList<GrammarRule> = mutableListOf(),
        val commonPatterns: MutableList<String> = mutableListOf(),
        val learningScore: Double = 0.0,
        val totalPhrases: Int = 0,
        val verifiedPhrases: Int = 0,
        val contributors: Int = 0,
        val difficulty: LanguageDifficulty = LanguageDifficulty.MODERATE,
        val isActive: Boolean = true
    )
    
    enum class LanguageDifficulty {
        EASY, MODERATE, HARD, COMPLEX
    }
    
    data class GrammarRule(
        val pattern: String,
        val explanation: String,
        val examples: List<String>
    )
    
    data class LearningSession(
        val language: String,
        val currentPhrase: Int = 0,
        val totalPhrases: Int = 0,
        val score: Int = 0,
        val streak: Int = 0,
        val mistakes: List<String> = emptyList(),
        val startTime: Long = System.currentTimeMillis()
    )
    
    // ═══════════════════════════════════════
    // INITIALIZE ALL LANGUAGES
    // ═══════════════════════════════════════
    
    init {
        initializeLanguages()
        loadSavedData()
    }
    
    private fun initializeLanguages() {
        _allLanguages.value = listOf(
            // Kenya
            LanguageProfile("kln", "Kalenjin", "Kalenjin", "🇰🇪", "Rift Valley", "6.3M",
                listOf("Nandi", "Kipsigis", "Tugen", "Keiyo", "Marakwet", "Pokot", "Sabaot", "Terik"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("sheng", "Sheng'", "Sheng'", "🇰🇪", "Urban Kenya", "5M+",
                listOf("Nairobi", "Mombasa", "Kisumu", "Nakuru"),
                difficulty = LanguageDifficulty.EASY),
            
            LanguageProfile("maa", "Maa", "ɔl Maa", "🇰🇪", "Rift Valley", "1.2M",
                listOf("Purko", "Kisongo", "Sampur", "Loitai"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("mer", "Kimeru", "Kĩmĩĩrũ", "🇰🇪", "Eastern", "2M",
                listOf("Tigania", "Igembe", "Imenti", "Tharaka"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("gus", "Kisii", "Ekegusii", "🇰🇪", "Nyanza", "2.7M",
                listOf("Bobasi", "Bonchari", "South Mugirango"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("emb", "Embu", "Kĩembu", "🇰🇪", "Eastern", "700K",
                listOf("Manyatta", "Runyenjes", "Mbeere"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("tuv", "Turkana", "Ng'aturkana", "🇰🇪", "Rift Valley", "1M",
                listOf("Central", "North", "South"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("pok", "Pokot", "Pökoot", "🇰🇪", "Rift Valley", "800K",
                listOf("East", "West"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("rend", "Rendille", "Rendille", "🇰🇪", "Eastern", "100K",
                listOf("Marsabit"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("baj", "Bajuni", "Kibajuni", "🇰🇪", "Coast", "100K",
                listOf("Lamu", "Tana", "Pate"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("ogd", "Ogiek", "Ogiek", "🇰🇪", "Rift Valley", "50K",
                listOf("Mau", "Elgon", "Aberdare"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("tes", "Teso", "Ateso", "🇰🇪", "Western", "400K",
                listOf("Busia"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("sml", "Somali", "Af-Soomaali", "🇰🇪", "North Eastern", "2.8M",
                listOf("Garissa", "Wajir", "Mandera"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("elg", "Elgeyo", "Elgeyo", "🇰🇪", "Rift Valley", "500K",
                listOf("Keiyo"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("sab", "Saboat", "Saboat", "🇰🇪", "Western", "300K",
                listOf("Mt Elgon", "Kitale"),
                difficulty = LanguageDifficulty.HARD),
            
            // Tanzania
            LanguageProfile("suk", "Sukuma", "Kisukuma", "🇹🇿", "Lake Zone", "8M",
                listOf("Mwanza", "Shinyanga"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("chg", "Chagga", "Kichagga", "🇹🇿", "Kilimanjaro", "2M",
                listOf("Moshi", "Marangu", "Rombo"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("haya", "Haya", "Kihaya", "🇹🇿", "Kagera", "2M",
                listOf("Bukoba", "Muleba"),
                difficulty = LanguageDifficulty.MODERATE),
            
            // Uganda
            LanguageProfile("lug", "Luganda", "Luganda", "🇺🇬", "Central", "16M",
                listOf("Kampala", "Mengo", "Mukono"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("ach", "Acholi", "Luo", "🇺🇬", "Northern", "1.5M",
                listOf("Gulu", "Kitgum"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("rny", "Runyankole", "Runyankore", "🇺🇬", "Western", "4M",
                listOf("Mbarara", "Bushenyi"),
                difficulty = LanguageDifficulty.MODERATE),
            
            // Nigeria
            LanguageProfile("ibo", "Igbo", "Igbo", "🇳🇬", "South East", "44M",
                listOf("Owerri", "Onitsha", "Enugu", "Aba"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("efi", "Efik", "Efik", "🇳🇬", "South South", "2M",
                listOf("Calabar", "Akwa Ibom"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("tiv", "Tiv", "Tiv", "🇳🇬", "Middle Belt", "5M",
                listOf("Makurdi", "Gboko"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("kan", "Kanuri", "Kanuri", "🇳🇬", "North East", "8M",
                listOf("Maiduguri", "Damaturu"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            // Ghana
            LanguageProfile("twi", "Twi", "Twi", "🇬🇭", "Ashanti", "18M",
                listOf("Kumasi", "Accra"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("gaa", "Ga", "Ga", "🇬🇭", "Greater Accra", "2M",
                listOf("Accra", "Tema"),
                difficulty = LanguageDifficulty.MODERATE),
            
            LanguageProfile("ewe", "Ewe", "Eʋegbe", "🇬🇭", "Volta", "5M",
                listOf("Ho", "Keta"),
                difficulty = LanguageDifficulty.HARD),
            
            // Ethiopia
            LanguageProfile("orm", "Oromo", "Afaan Oromoo", "🇪🇹", "Oromia", "40M",
                listOf("Adama", "Jimma", "Harar"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("tig", "Tigrinya", "ትግርኛ", "🇪🇹", "Tigray", "10M",
                listOf("Mekelle", "Adwa"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("sid", "Sidamo", "Sidaamu Afoo", "🇪🇹", "Southern", "4M",
                listOf("Hawassa"),
                difficulty = LanguageDifficulty.HARD),
            
            // South Africa
            LanguageProfile("xho", "Xhosa", "isiXhosa", "🇿🇦", "Eastern Cape", "8M",
                listOf("Mthatha", "East London"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("ven", "Venda", "Tshivenḓa", "🇿🇦", "Limpopo", "1.5M",
                listOf("Thohoyandou"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("tso", "Tsonga", "Xitsonga", "🇿🇦", "Mpumalanga", "2M",
                listOf("Nelspruit", "Giyani"),
                difficulty = LanguageDifficulty.HARD),
            
            LanguageProfile("sot", "Sotho", "Sesotho", "🇿🇦", "Free State", "5M",
                listOf("Bloemfontein", "Maseru"),
                difficulty = LanguageDifficulty.MODERATE),
            
            // Sudan
            LanguageProfile("din", "Dinka", "Thuɔŋjäŋ", "🇸🇩", "South Sudan", "5M",
                listOf("Juba", "Bor", "Malakal"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            LanguageProfile("nub", "Nubian", "Nobiin", "🇸🇩", "Northern", "1M",
                listOf("Dongola", "Wadi Halfa"),
                difficulty = LanguageDifficulty.COMPLEX),
            
            // DRC
            LanguageProfile("lin", "Lingala", "Lingála", "🇨🇩", "Kinshasa", "40M",
                listOf("Kinshasa", "Brazzaville"),
                difficulty = LanguageDifficulty.EASY),
            
            LanguageProfile("lub", "Luba", "Tshiluba", "🇨🇩", "Kasai", "7M",
                listOf("Mbuji-Mayi"),
                difficulty = LanguageDifficulty.HARD)
        )
    }
    
    // ═══════════════════════════════════════
    // AI LEARNING METHODS
    // ═══════════════════════════════════════
    
    /**
     * AI learns from pattern recognition in user contributions
     */
    fun learnFromPatterns(languageCode: String) {
        val language = _allLanguages.value.find { it.code == languageCode } ?: return
        val phrases = language.phrases
        
        // Detect common prefixes
        val prefixes = mutableMapOf<String, Int>()
        phrases.values.forEach { phrase ->
            (1..minOf(4, phrase.length)).forEach { len ->
                val prefix = phrase.take(len)
                prefixes[prefix] = (prefixes[prefix] ?: 0) + 1
            }
        }
        
        // Detect common suffixes
        val suffixes = mutableMapOf<String, Int>()
        phrases.values.forEach { phrase ->
            (1..minOf(4, phrase.length)).forEach { len ->
                val suffix = phrase.takeLast(len)
                suffixes[suffix] = (suffixes[suffix] ?: 0) + 1
            }
        }
        
        // Add grammar rules
        val commonPrefixes = prefixes.filter { it.value >= 3 }.keys.take(5)
        val commonSuffixes = suffixes.filter { it.value >= 3 }.keys.take(5)
        
        commonPrefixes.forEach { prefix ->
            val words = phrases.values.filter { it.startsWith(prefix) }.take(5)
            if (words.size >= 3) {
                language.grammarRules.add(GrammarRule(
                    pattern = "Words starting with '$prefix'",
                    explanation = "Common prefix found in ${phrases.count { it.value.startsWith(prefix) }} words",
                    examples = words
                ))
            }
        }
        
        commonSuffixes.forEach { suffix ->
            val words = phrases.values.filter { it.endsWith(suffix) }.take(5)
            if (words.size >= 3) {
                language.grammarRules.add(GrammarRule(
                    pattern = "Words ending with '$suffix'",
                    explanation = "Common suffix found in ${phrases.count { it.value.endsWith(suffix) }} words",
                    examples = words
                ))
            }
        }
    }
    
    /**
     * AI predicts translations based on patterns
     */
    fun predictTranslation(
        englishPhrase: String,
        languageCode: String,
        confidence: Double = 0.0
    ): PredictedTranslation {
        val language = _allLanguages.value.find { it.code == languageCode }
        if (language == null) return PredictedTranslation("", 0.0, "Language not found")
        
        val existingPhrases = language.phrases
        
        // Check exact match
        existingPhrases[englishPhrase.lowercase()]?.let {
            return PredictedTranslation(it, 1.0, "Exact match")
        }
        
        // Check partial matches
        val words = englishPhrase.lowercase().split(" ")
        val partialMatches = mutableListOf<Pair<String, Double>>()
        
        words.forEach { word ->
            existingPhrases.entries.forEach { (eng, loc) ->
                if (eng.contains(word) || word.contains(eng)) {
                    partialMatches.add(loc to 0.5)
                }
            }
        }
        
        if (partialMatches.isNotEmpty()) {
            // Use grammar rules to combine partial matches
            val combined = partialMatches.joinToString(" ") { it.first }
            return PredictedTranslation(combined, 0.4, "Partial match from patterns")
        }
        
        // Use grammar rules to generate translation
        val rules = language.grammarRules
        if (rules.isNotEmpty() && words.size == 1 && words[0].endsWith("ing")) {
            val root = words[0].removeSuffix("ing")
            rules.find { it.pattern.contains("verb") || it.pattern.contains("action") }?.let { rule ->
                val generated = rule.examples.firstOrNull() ?: root
                return PredictedTranslation(generated, 0.2, "Generated from grammar rules")
            }
        }
        
        return PredictedTranslation(englishPhrase, 0.0, "No prediction available")
    }
    
    data class PredictedTranslation(
        val translation: String,
        val confidence: Double,
        val source: String
    )
    
    /**
     * AI-powered learning session
     */
    fun startLearningSession(languageCode: String): LearningSession {
        val language = _allLanguages.value.find { it.code == languageCode }
        val phrases = language?.phrases ?: emptyMap()
        
        val session = LearningSession(
            language = languageCode,
            totalPhrases = minOf(10, phrases.size),
            currentPhrase = 0
        )
        
        _learningSessions.value = session
        return session
    }
    
    fun answerInSession(answer: String): Boolean {
        val session = _learningSessions.value ?: return false
        val language = _allLanguages.value.find { it.code == session.language } ?: return false
        val phrases = language.phrases.entries.toList()
        
        if (session.currentPhrase >= phrases.size) return false
        
        val (english, correctAnswer) = phrases[session.currentPhrase]
        val isCorrect = answer.equals(correctAnswer, ignoreCase = true)
        
        _learningSessions.value = session.copy(
            currentPhrase = session.currentPhrase + 1,
            score = if (isCorrect) session.score + 1 else session.score,
            streak = if (isCorrect) session.streak + 1 else 0,
            mistakes = if (!isCorrect) session.mistakes + english else session.mistakes
        )
        
        return isCorrect
    }
    
    /**
     * Get language suggestions based on user's location
     */
    fun getSuggestedLanguages(userLocation: String? = null): List<LanguageProfile> {
        return when {
            userLocation?.contains("Kenya", true) == true -> 
                _allLanguages.value.filter { it.region.contains("Kenya") || it.region == "Urban Kenya" }
            userLocation?.contains("Tanzania", true) == true ->
                _allLanguages.value.filter { it.region.contains("Tanzania") }
            userLocation?.contains("Uganda", true) == true ->
                _allLanguages.value.filter { it.region.contains("Uganda") }
            userLocation?.contains("Nigeria", true) == true ->
                _allLanguages.value.filter { it.region.contains("Nigeria") }
            else -> _allLanguages.value.take(10)
        }
    }
    
    /**
     * Get trending phrases (most recently added)
     */
    fun getTrendingPhrases(languageCode: String, limit: Int = 10): List<Pair<String, String>> {
        val language = _allLanguages.value.find { it.code == languageCode } ?: return emptyList()
        return language.phrases.entries.takeLast(limit).map { it.key to it.value }
    }
    
    /**
     * Get daily challenge
     */
    fun getDailyChallenge(languageCode: String): List<Pair<String, List<String>>> {
        val language = _allLanguages.value.find { it.code == languageCode } ?: return emptyList()
        val phrases = language.phrases.entries.shuffled().take(5)
        
        return phrases.map { (english, correct) ->
            val wrongAnswers = language.phrases.values
                .filter { it != correct }
                .shuffled()
                .take(3)
            english to (wrongAnswers + correct).shuffled()
        }
    }
    
    private fun saveData() {
        try {
            val json = JSONObject()
            _allLanguages.value.forEach { lang ->
                val langJson = JSONObject()
                langJson.put("code", lang.code)
                langJson.put("phrases", JSONObject(lang.phrases))
                langJson.put("learningScore", lang.learningScore)
                langJson.put("verifiedPhrases", lang.verifiedPhrases)
                langJson.put("contributors", lang.contributors)
                json.put(lang.code, langJson)
            }
            dataFile.writeText(json.toString())
        } catch (e: Exception) { }
    }
    
    private fun loadSavedData() {
        try {
            if (!dataFile.exists()) return
            val json = JSONObject(dataFile.readText())
            val updated = _allLanguages.value.toMutableList()
            
            updated.forEachIndexed { index, lang ->
                json.optJSONObject(lang.code)?.let { saved ->
                    val phrases = JSONObject(saved.optString("phrases", "{}"))
                    val phrasesMap = mutableMapOf<String, String>()
                    phrases.keys().forEach { key ->
                        phrasesMap[key] = phrases.getString(key)
                    }
                    updated[index] = lang.copy(
                        phrases = phrasesMap,
                        learningScore = saved.optDouble("learningScore", 0.0),
                        verifiedPhrases = saved.optInt("verifiedPhrases", 0),
                        contributors = saved.optInt("contributors", 0)
                    )
                }
            }
            _allLanguages.value = updated
        } catch (e: Exception) { }
    }
    
    fun saveProgress() = saveData()
}
