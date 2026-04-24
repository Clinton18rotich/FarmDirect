package com.farmdirect.app.ui.screens.language

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.domain.ml.AILanguageLearner
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AILanguageScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val engine = remember { AILanguageLearner(context) }
    val languages by engine.allLanguages.collectAsState()
    val session by engine.learningSession.collectAsState()
    
    var selectedLanguage by remember { mutableStateOf<AILanguageLearner.LanguageProfile?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var userAnswer by remember { mutableStateOf("") }
    var showQuiz by remember { mutableStateOf(false) }
    var quizQuestions by remember { mutableStateOf<List<Pair<String, List<String>>>>(emptyList()) }
    var quizScore by remember { mutableIntStateOf(0) }
    var currentQuestion by remember { mutableIntStateOf(0) }
    
    val pulseScale by rememberInfiniteTransition().animateFloat(1f, 1.05f, infiniteRepeatable(tween(1500), RepeatMode.Reverse))
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Language Learner", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            // Stats Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("🗣️", "${languages.size}", "Languages")
                        StatItem("📚", "${languages.sumOf { it.phrases.size }}", "Phrases")
                        StatItem("✅", "${languages.sumOf { it.verifiedPhrases }}", "Verified")
                        StatItem("👥", "${languages.sumOf { it.contributors }}", "Learners")
                    }
                }
            }
            
            // Learning Session Banner
            if (session != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PrimaryGreen.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.School, null, tint = PrimaryGreen, modifier = Modifier.size(32.dp).animateScale(pulseScale))
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Learning Session Active", fontWeight = FontWeight.Bold, color = PrimaryDarkGreen)
                                Text("${session!!.currentPhrase}/${session!!.totalPhrases} • Score: ${session!!.score} • Streak: ${session!!.streak}🔥", color = TextSecondary, fontSize = 13.sp)
                            }
                            Button(onClick = { /* Continue session */ }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)) {
                                Text("Continue")
                            }
                        }
                    }
                }
            }
            
            // Search
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search 38 African languages...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
            }
            
            // Region Tabs
            item {
                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listOf("🇰🇪 Kenya", "🇹🇿 Tanzania", "🇺🇬 Uganda", "🇳🇬 Nigeria", "🇬🇭 Ghana", "🇪🇹 Ethiopia", "🇿🇦 S. Africa", "🇨🇩 DRC", "🌍 All")) { region ->
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = { Text(region, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryGreen)
                        )
                    }
                }
            }
            
            // Language Cards
            items(languages.filter { searchQuery.isBlank() || it.name.contains(searchQuery, true) || it.nativeName.contains(searchQuery, true) }) { language ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { selectedLanguage = language },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedLanguage?.code == language.code) Green50 else SurfaceWhite
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(language.flag, fontSize = 36.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row {
                                    Text("${language.name} ", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("(${language.nativeName})", color = TextSecondary, fontSize = 14.sp)
                                }
                                Text("${language.speakers} speakers • ${language.region}", color = TextSecondary, fontSize = 12.sp)
                                Text(language.dialects.joinToString(" • "), color = PrimaryLightGreen, fontSize = 11.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${language.phrases.size}", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 18.sp)
                                Text("phrases", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        
                        if (language.learningScore > 0) {
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LinearProgressIndicator(
                                    progress = language.learningScore.toFloat(),
                                    modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                                    color = PrimaryGreen,
                                    trackColor = Green100
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("${(language.learningScore * 100).toInt()}%", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 13.sp)
                            }
                        }
                        
                        // Difficulty badge
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DifficultyBadge(language.difficulty)
                            Spacer(Modifier.width(8.dp))
                            Text("${language.verifiedPhrases} verified • ${language.contributors} contributors", color = TextSecondary, fontSize = 11.sp)
                        }
                        
                        // Action buttons
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = { engine.startLearningSession(language.code) }) {
                                Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                                Text("Learn", fontSize = 13.sp)
                            }
                            TextButton(onClick = {
                                quizQuestions = engine.getDailyChallenge(language.code)
                                showQuiz = true
                                currentQuestion = 0
                                quizScore = 0
                            }) {
                                Icon(Icons.Default.Quiz, null, modifier = Modifier.size(16.dp))
                                Text("Quiz", fontSize = 13.sp)
                            }
                            TextButton(onClick = { /* View phrases */ }) {
                                Icon(Icons.Default.MenuBook, null, modifier = Modifier.size(16.dp))
                                Text("Phrases", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
        
        // Quiz Dialog
        if (showQuiz && quizQuestions.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { showQuiz = false },
                title = {
                    Text("Daily Challenge 🏆", fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text(
                            "Question ${currentQuestion + 1}/${quizQuestions.size}",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Translate: \"${quizQuestions[currentQuestion].first}\"",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(12.dp))
                        quizQuestions[currentQuestion].second.forEach { option ->
                            OutlinedButton(
                                onClick = {
                                    val correct = engine.getAllLanguagesWithProgress()
                                        .firstOrNull { it.code == selectedLanguage?.code }
                                    if (option == correct?.name) quizScore++
                                    if (currentQuestion < quizQuestions.size - 1) currentQuestion++
                                    else showQuiz = false
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(option)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showQuiz = false }) {
                        Text("End Quiz (Score: $quizScore/${quizQuestions.size})")
                    }
                }
            )
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: AILanguageLearner.LanguageDifficulty) {
    val (color, text) = when (difficulty) {
        AILanguageLearner.LanguageDifficulty.EASY -> PrimaryGreen to "Easy"
        AILanguageLearner.LanguageDifficulty.MODERATE -> AccentGold to "Moderate"
        AILanguageLearner.LanguageDifficulty.HARD -> AccentOrange to "Hard"
        AILanguageLearner.LanguageDifficulty.COMPLEX -> StatusError to "Complex"
    }
    Surface(shape = RoundedCornerShape(6.dp), color = color.copy(alpha = 0.1f)) {
        Text(text, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = color, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun StatItem(emoji: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 28.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = PrimaryGreen)
        Text(label, color = TextSecondary, fontSize = 11.sp)
    }
}
