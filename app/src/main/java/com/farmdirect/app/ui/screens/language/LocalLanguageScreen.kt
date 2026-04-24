package com.farmdirect.app.ui.screens.language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.domain.ml.LocalLanguageEngine
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalLanguageScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val engine = remember { LocalLanguageEngine(context) }
    val languages by engine.learningProgress.collectAsState()
    
    var selectedLanguage by remember { mutableStateOf<LocalLanguageEngine.LocalLanguage?>(null) }
    var newEnglish by remember { mutableStateOf("") }
    var newLocal by remember { mutableStateOf("") }
    var showAddPhrase by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Learn Local Languages", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight,
        floatingActionButton = {
            if (selectedLanguage != null) {
                FloatingActionButton(
                    onClick = { showAddPhrase = true },
                    containerColor = AccentGold
                ) {
                    Icon(Icons.Default.Add, "Add Phrase", tint = TextWhite)
                }
            }
        }
    ) { padding ->
        if (showAddPhrase && selectedLanguage != null) {
            // Add Phrase Dialog
            AlertDialog(
                onDismissRequest = { showAddPhrase = false },
                title = { Text("Add ${selectedLanguage!!.name} Phrase") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newEnglish,
                            onValueChange = { newEnglish = it },
                            label = { Text("English Word/Phrase") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newLocal,
                            onValueChange = { newLocal = it },
                            label = { Text("${selectedLanguage!!.name} Translation") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newEnglish.isNotBlank() && newLocal.isNotBlank()) {
                                engine.addPhrase(
                                    language = selectedLanguage!!.code,
                                    englishPhrase = newEnglish,
                                    localPhrase = newLocal
                                )
                                newEnglish = ""
                                newLocal = ""
                                showAddPhrase = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                    ) {
                        Text("Save Phrase")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddPhrase = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
        
        LazyColumn(modifier = Modifier.padding(padding)) {
            // Search
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search phrases...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
            }
            
            // Language Cards
            items(engine.getAllLanguagesWithProgress()) { lang ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable { selectedLanguage = engine.supportedLocalLanguages.find { it.code == lang.code } },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedLanguage?.code == lang.code) Green50 else SurfaceWhite
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(lang.flag, fontSize = 32.sp)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(lang.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(lang.dialects.joinToString(", "), color = TextSecondary, fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${lang.phrasesCount}", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 18.sp)
                                Text("phrases", color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                        
                        if (lang.progress > 0) {
                            Spacer(Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = lang.progress.toFloat(),
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = PrimaryGreen,
                                trackColor = Green100
                            )
                            Text("${lang.verifiedCount} verified • ${(lang.progress * 100).toInt()}% complete", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
            
            // Phrases for selected language
            if (selectedLanguage != null) {
                item {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "📖 ${selectedLanguage!!.name} Phrases",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                
                val phrases = engine.getAllPhrasesForLanguage(selectedLanguage!!.code)
                    .filter { searchQuery.isBlank() || it.key.contains(searchQuery, true) || it.value.contains(searchQuery, true) }
                
                if (phrases.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📝", fontSize = 48.sp)
                                Text("No phrases found", color = TextSecondary)
                                Text("Tap + to add the first phrase!", color = TextSecondary, fontSize = 13.sp)
                            }
                        }
                    }
                } else {
                    items(phrases.entries.toList()) { (english, local) ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(english, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                    Text(local, color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                }
                                IconButton(onClick = {
                                    engine.verifyPhrase(selectedLanguage!!.code, english)
                                }) {
                                    Icon(Icons.Default.ThumbUp, "Verify", tint = PrimaryGreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
