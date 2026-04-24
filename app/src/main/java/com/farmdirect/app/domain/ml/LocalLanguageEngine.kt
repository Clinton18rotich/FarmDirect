package com.farmdirect.app.domain.ml

import android.content.Context
import com.farmdirect.app.util.LanguageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class LocalLanguageEngine(private val context: Context) {
    
    private val _learnedPhrases = MutableStateFlow<Map<String, Map<String, String>>>(emptyMap())
    val learnedPhrases = _learnedPhrases.asStateFlow()
    
    private val _learningProgress = MutableStateFlow<Map<String, Double>>(emptyMap())
    val learningProgress = _learningProgress.asStateFlow()
    
    private val dataFile: File
        get() = File(context.filesDir, "local_languages.json")
    
    // ═══════════════════════════════════════
    // LANGUAGE DATABASE
    // ═══════════════════════════════════════
    
    val supportedLocalLanguages = listOf(
        LocalLanguage("kln", "Kalenjin", "🇰🇪", listOf("Nandi", "Kipsigis", "Tugen", "Keiyo", "Marakwet", "Pokot", "Sabaot")),
        LocalLanguage("sheng", "Sheng'", "🇰🇪", listOf("Nairobi", "Urban", "Youth")),
        LocalLanguage("maa", "Maa (Maasai)", "🇰🇪", listOf("Purko", "Kisongo", "Sampur")),
        LocalLanguage("mer", "Kimeru", "🇰🇪", listOf("Tigania", "Igembe", "Imenti")),
        LocalLanguage("emb", "Embu", "🇰🇪", listOf("Manyatta", "Runyenjes")),
        LocalLanguage("gus", "Kisii (Ekegusii)", "🇰🇪", listOf("Bobasi", "Bonchari")),
        LocalLanguage("tuv", "Turkana", "🇰🇪", listOf("Central", "North")),
        LocalLanguage("baj", "Bajuni", "🇰🇪", listOf("Lamu", "Tana")),
        LocalLanguage("pok", "Pokot", "🇰🇪", listOf("East", "West")),
        LocalLanguage("ogd", "Ogiek", "🇰🇪", listOf("Mau", "Elgon")),
        LocalLanguage("rend", "Rendille", "🇰🇪", listOf("Marsabit")),
        LocalLanguage("elg", "Elgeyo", "🇰🇪", listOf("Keiyo")),
        LocalLanguage("sab", "Saboat", "🇰🇪", listOf("Mt Elgon")),
        LocalLanguage("tes", "Teso", "🇰🇪", listOf("Busia")),
        LocalLanguage("sml", "Somali (Kenyan)", "🇰🇪", listOf("Garissa", "Wajir", "Mandera"))
    )
    
    data class LocalLanguage(
        val code: String,
        val name: String,
        val flag: String,
        val dialects: List<String>
    )
    
    // ═══════════════════════════════════════
    // PRE-LOADED PHRASES
    // ═══════════════════════════════════════
    
    private val basePhrases = mapOf(
        "kln" to mapOf(
            // Kalenjin - Nandi dialect
            "hello" to "Chamge",
            "good_morning" to "Chamgei",
            "how_are_you" to "Iyamune?",
            "fine" to "Mising",
            "thank_you" to "Kongoi",
            "welcome" to "Kiboren",
            "goodbye" to "Sait sere",
            "yes" to "Ee",
            "no" to "Acha",
            "farmer" to "Kibikchi",
            "crops" to "Cheptabushek",
            "maize" to "Chepbanda",
            "buy" to "Kial",
            "sell" to "Kialdai",
            "price" to "Beibei",
            "good" to "Karang",
            "water" to "Bek",
            "sun" to "Asista",
            "rain" to "Robta",
            "harvest" to "Bunun",
            "market" to "Sokot",
            "home" to "Kaa",
            "food" to "Amis",
            "milk" to "Chego",
            "cow" to "Teta",
            "goat" to "Ng'ok",
            "sheep" to "Kechir",
            "chicken" to "Ingok",
            "farm" to "Barak"
        ),
        
        "sheng" to mapOf(
            // Sheng' - Urban youth language
            "hello" to "Niaje",
            "how_are_you" to "Vipi?",
            "fine" to "Poa",
            "thank_you" to "Shukran",
            "goodbye" to "Badaaye",
            "yes" to "Eeh",
            "no" to "Aaai",
            "friend" to "Beste",
            "good" to "Fiti",
            "bad" to "Mbaya",
            "money" to "Dough",
            "price" to "Bei",
            "buy" to "Buy",
            "sell" to "Sell",
            "food" to "Chop",
            "water" to "Maji",
            "home" to "Keja",
            "car" to "Moti",
            "phone" to "Simu",
            "work" to "Job",
            "market" to "Soko",
            "cheap" to "Poa",
            "expensive" to "Gharama",
            "customer" to "Mteja",
            "delivery" to "Delivery"
        ),
        
        "maa" to mapOf(
            // Maasai
            "hello" to "Sopa",
            "how_are_you" to "Ira iyiook?",
            "fine" to "Sidai",
            "thank_you" to "Ashe",
            "welcome" to "Karibu",
            "goodbye" to "Ole sere",
            "yes" to "Eeh",
            "no" to "Mm mm",
            "cow" to "Enkiteng",
            "goat" to "Enkine",
            "sheep" to "Enker",
            "milk" to "Kule",
            "water" to "Enkare",
            "food" to "Endaa",
            "home" to "Enkang",
            "market" to "Esoko",
            "price" to "Ebei"
        )
    )
    
    // ═══════════════════════════════════════
    // LEARNING ENGINE
    // ═══════════════════════════════════════
    
    data class LearnedPhrase(
        val language: String,
        val englishPhrase: String,
        val localPhrase: String,
        val contributorPhone: String? = null,
        val contributorLocation: String? = null,
        val verified: Boolean = false,
        val verificationCount: Int = 0,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    private val userLearnedPhrases = mutableListOf<LearnedPhrase>()
    
    init {
        loadData()
    }
    
    /**
     * Add a new phrase learned from a user
     */
    fun addPhrase(
        language: String,
        englishPhrase: String,
        localPhrase: String,
        contributorPhone: String? = null,
        contributorLocation: String? = null
    ): LearnedPhrase {
        val phrase = LearnedPhrase(
            language = language,
            englishPhrase = englishPhrase.lowercase().trim(),
            localPhrase = localPhrase.trim(),
            contributorPhone = contributorPhone,
            contributorLocation = contributorLocation
        )
        userLearnedPhrases.add(phrase)
        
        // Update the phrases map
        val currentPhrases = _learnedPhrases.value.toMutableMap()
        val langPhrases = currentPhrases.getOrPut(language) { mutableMapOf() }.toMutableMap()
        langPhrases[englishPhrase.lowercase().trim()] = localPhrase.trim()
        currentPhrases[language] = langPhrases
        _learnedPhrases.value = currentPhrases
        
        // Update learning progress
        updateProgress(language)
        
        saveData()
        return phrase
    }
    
    /**
     * Verify a phrase (community validation)
     */
    fun verifyPhrase(language: String, englishPhrase: String): LearnedPhrase? {
        val phrase = userLearnedPhrases.find {
            it.language == language && it.englishPhrase == englishPhrase.lowercase()
        }
        phrase?.let {
            val updated = it.copy(
                verificationCount = it.verificationCount + 1,
                verified = it.verificationCount + 1 >= 3
            )
            userLearnedPhrases.remove(it)
            userLearnedPhrases.add(updated)
            return updated
        }
        return null
    }
    
    /**
     * Translate a phrase
     */
    fun translate(phrase: String, language: String): String {
        val key = phrase.lowercase().trim()
        
        // Check base phrases first
        val baseTranslation = basePhrases[language]?.get(key)
        if (baseTranslation != null) return baseTranslation
        
        // Check learned phrases
        val learnedTranslation = _learnedPhrases.value[language]?.get(key)
        if (learnedTranslation != null) return learnedTranslation
        
        // Try to guess from similar phrases
        return guessTranslation(key, language) ?: phrase
    }
    
    /**
     * Detect which local language a message is in
     */
    fun detectLanguage(text: String): Map<String, Double> {
        val scores = mutableMapOf<String, Double>()
        val words = text.lowercase().split(" ")
        
        supportedLocalLanguages.forEach { lang ->
            val phrases = getAllPhrasesForLanguage(lang.code)
            var matchCount = 0
            
            words.forEach { word ->
                if (phrases.values.any { it.contains(word, ignoreCase = true) }) {
                    matchCount++
                }
                if (phrases.keys.any { word.contains(it, ignoreCase = true) }) {
                    matchCount++
                }
            }
            
            val score = if (words.isNotEmpty()) matchCount.toDouble() / words.size else 0.0
            if (score > 0) scores[lang.code] = score
        }
        
        return scores.entries
            .sortedByDescending { it.value }
            .associate { it.toPair() }
    }
    
    /**
     * Get all phrases for a language (base + learned)
     */
    fun getAllPhrasesForLanguage(language: String): Map<String, String> {
        val base = basePhrases[language] ?: emptyMap()
        val learned = _learnedPhrases.value[language] ?: emptyMap()
        return base + learned
    }
    
    /**
     * Get learning progress for a language
     */
    fun getProgress(language: String): Double {
        return _learningProgress.value[language] ?: 0.0
    }
    
    /**
     * Get all available languages with progress
     */
    fun getAllLanguagesWithProgress(): List<LanguageWithProgress> {
        return supportedLocalLanguages.map { lang ->
            LanguageWithProgress(
                code = lang.code,
                name = lang.name,
                flag = lang.flag,
                dialects = lang.dialects,
                phrasesCount = getAllPhrasesForLanguage(lang.code).size,
                verifiedCount = userLearnedPhrases.count { it.language == lang.code && it.verified },
                progress = getProgress(lang.code)
            )
        }
    }
    
    /**
     * Suggest new phrases to learn based on user's activity
     */
    fun suggestPhrases(language: String, userContext: String = "farming"): List<String> {
        val existingPhrases = getAllPhrasesForLanguage(langCode = language)
        val suggestions = mutableListOf<String>()
        
        val farmingPhrases = when (userContext) {
            "farming" -> listOf("plant", "harvest", "irrigation", "fertilizer", "pesticide", "storage")
            "livestock" -> listOf("vaccination", "breeding", "feeding", "milking", "slaughter")
            "market" -> listOf("bargain", "wholesale", "retail", "discount", "invoice")
            "weather" -> listOf("storm", "drought", "flood", "humidity", "forecast")
            else -> listOf("hello", "thank_you", "how_are_you", "goodbye")
        }
        
        farmingPhrases.forEach { phrase ->
            if (!existingPhrases.containsKey(phrase)) {
                suggestions.add(phrase)
            }
        }
        
        return suggestions
    }
    
    /**
     * Learn from SMS messages
     */
    fun learnFromSMS(message: String, senderLocation: String? = null): List<LearnedPhrase> {
        val newPhrases = mutableListOf<LearnedPhrase>()
        
        // Pattern: "In [Language], [English] is [Local]"
        val pattern1 = Regex("in (\\w+),?\\s*\"?([^\"]+?)\"?\\s+is\\s+\"?(.+?)\"?\\s*$", RegexOption.IGNORE_CASE)
        val match1 = pattern1.find(message)
        if (match1 != null) {
            val lang = match1.groupValues[1].lowercase()
            val english = match1.groupValues[2].trim()
            val local = match1.groupValues[3].trim()
            
            val localLang = supportedLocalLanguages.find { 
                it.name.equals(lang, ignoreCase = true) || it.code == lang 
            }
            if (localLang != null) {
                val phrase = addPhrase(localLang.code, english, local, contributorLocation = senderLocation)
                newPhrases.add(phrase)
            }
        }
        
        // Pattern: "[English] = [Local] in [Language]"
        val pattern2 = Regex("\"?(.+?)\"?\\s*=\\s*\"?(.+?)\"?\\s+in\\s+(\\w+)", RegexOption.IGNORE_CASE)
        val match2 = pattern2.find(message)
        if (match2 != null) {
            val english = match2.groupValues[1].trim()
            val local = match2.groupValues[2].trim()
            val lang = match2.groupValues[3].lowercase()
            
            val localLang = supportedLocalLanguages.find { 
                it.name.equals(lang, ignoreCase = true) || it.code == lang 
            }
            if (localLang != null) {
                val phrase = addPhrase(localLang.code, english, local, contributorLocation = senderLocation)
                newPhrases.add(phrase)
            }
        }
        
        return newPhrases
    }
    
    private fun guessTranslation(phrase: String, language: String): String? {
        // Look for similar words in the same language
        val allPhrases = getAllPhrasesForLanguage(language)
        
        // Check if any known phrase contains this word
        allPhrases.entries.forEach { (eng, loc) ->
            if (eng.contains(phrase, ignoreCase = true) || phrase.contains(eng, ignoreCase = true)) {
                return loc
            }
        }
        
        return null
    }
    
    private fun updateProgress(language: String) {
        val total = getAllPhrasesForLanguage(language).size
        val verified = userLearnedPhrases.count { it.language == language && it.verified }
        val progress = if (total > 0) (verified.toDouble() / total).coerceIn(0.0, 1.0) else 0.0
        
        val current = _learningProgress.value.toMutableMap()
        current[language] = progress
        _learningProgress.value = current
    }
    
    private fun saveData() {
        try {
            val json = JSONObject()
            val arr = JSONArray()
            userLearnedPhrases.forEach { phrase ->
                arr.put(JSONObject().apply {
                    put("language", phrase.language)
                    put("englishPhrase", phrase.englishPhrase)
                    put("localPhrase", phrase.localPhrase)
                    put("contributorPhone", phrase.contributorPhone ?: "")
                    put("contributorLocation", phrase.contributorLocation ?: "")
                    put("verified", phrase.verified)
                    put("verificationCount", phrase.verificationCount)
                    put("timestamp", phrase.timestamp)
                })
            }
            json.put("phrases", arr)
            dataFile.writeText(json.toString())
        } catch (e: Exception) { }
    }
    
    private fun loadData() {
        try {
            if (dataFile.exists()) {
                val json = JSONObject(dataFile.readText())
                val arr = json.getJSONArray("phrases")
                repeat(arr.length()) { i ->
                    val obj = arr.getJSONObject(i)
                    userLearnedPhrases.add(LearnedPhrase(
                        language = obj.getString("language"),
                        englishPhrase = obj.getString("englishPhrase"),
                        localPhrase = obj.getString("localPhrase"),
                        contributorPhone = obj.optString("contributorPhone", null),
                        contributorLocation = obj.optString("contributorLocation", null),
                        verified = obj.optBoolean("verified", false),
                        verificationCount = obj.optInt("verificationCount", 0),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    ))
                }
                // Rebuild phrases map
                val rebuilt = mutableMapOf<String, MutableMap<String, String>>()
                userLearnedPhrases.forEach { phrase ->
                    val langPhrases = rebuilt.getOrPut(phrase.language) { mutableMapOf() }
                    langPhrases[phrase.englishPhrase] = phrase.localPhrase
                }
                _learnedPhrases.value = rebuilt.mapValues { it.value.toMap() }
            }
        } catch (e: Exception) { }
    }
    
    data class LanguageWithProgress(
        val code: String,
        val name: String,
        val flag: String,
        val dialects: List<String> = emptyList(),
        val phrasesCount: Int = 0,
        val verifiedCount: Int = 0,
        val progress: Double = 0.0
    )
}
