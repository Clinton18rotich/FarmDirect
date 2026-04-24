package com.farmdirect.app.ui.screens.video

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFeedScreen(onBack: () -> Unit, viewModel: VideoViewModel = hiltViewModel()) {
    val videos by viewModel.videos.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadFeed() }
    Scaffold(topBar = { TopAppBar(title = { Text("Farm Videos", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(videos) { video ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable { }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(12.dp)) {
                        AsyncImage(model = video.thumbnailUrl, null, Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text(video.title, fontWeight = FontWeight.Bold); Text(video.creatorName, color = TextSecondary, fontSize = 13.sp); Row { Icon(Icons.Default.Visibility, null, Modifier.size(14.dp), tint = TextSecondary); Text(" ${video.views}", color = TextSecondary, fontSize = 12.sp) } }
                    }
                }
            }
        }
    }
}
