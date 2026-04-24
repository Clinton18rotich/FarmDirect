package com.farmdirect.app.ui.screens.language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*
import com.farmdirect.app.util.LanguageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val currentLang = LanguageManager.getCurrentLanguage(context)
    var selectedLang by remember { mutableStateOf(currentLang) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Language / Chagua Lugha", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(LanguageManager.supportedLanguages.entries.toList()) { entry ->
                val (code, name) = entry.key
                val flag = entry.value
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clickable {
                            selectedLang = code
                            LanguageManager.setLocale(context, code)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedLang == code) Green50 else SurfaceWhite
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(flag, fontSize = 28.sp)
                        Spacer(Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Text(code.uppercase(), color = TextSecondary, fontSize = 13.sp)
                        }
                        if (selectedLang == code) {
                            Icon(Icons.Default.CheckCircle, null, tint = PrimaryGreen)
                        }
                    }
                }
            }
        }
    }
}
