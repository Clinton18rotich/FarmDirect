package com.farmdirect.app.ui.screens.ai

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiseaseDetectionScreen(onBack: () -> Unit, viewModel: AIViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    var selectedCrop by remember { mutableStateOf("Maize") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> uri?.let { imageUri = it; val bitmap = android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, it); viewModel.detectDisease(bitmap, selectedCrop) } }

    Scaffold(topBar = { TopAppBar(title = { Text("AI Disease Detective", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(20.dp)) {
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth().height(250.dp)) {
                if (imageUri != null) AsyncImage(model = imageUri, null, Modifier.fillMaxSize().clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop)
                else Box(Modifier.fillMaxSize().clickable { imagePicker.launch("image/*") }, contentAlignment = Alignment.Center) { Column(horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.CameraAlt, null, Modifier.size(64.dp), tint = PrimaryGreen); Text("Tap to take photo", color = TextSecondary) } }
            }
            Spacer(Modifier.height(16.dp))
            state.diseaseDetection?.let { d ->
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Green50)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("🔬 Diagnosis: ${d.disease}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text("Confidence: ${(d.confidence * 100).toInt()}% • Severity: ${d.severity}", color = TextSecondary)
                        Spacer(Modifier.height(12.dp))
                        Text("Treatment: ${d.treatment.name}", fontWeight = FontWeight.SemiBold)
                        Text("💊 ${d.treatment.dosage} • Cost: KES ${d.treatment.costEstimate}")
                    }
                }
            }
        }
    }
}
