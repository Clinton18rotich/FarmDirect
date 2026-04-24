package com.farmdirect.app.ui.screens.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(onChatClick: (String) -> Unit, viewModel: ChatViewModel = hiltViewModel()) {
    val chats by viewModel.chats.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadChats() }
    Scaffold(topBar = { TopAppBar(title = { Text("Messages", color = TextWhite) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        if (chats.isEmpty()) Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("No messages yet 💬", color = TextSecondary) }
        else LazyColumn(modifier = Modifier.padding(padding)) {
            items(chats) { chat ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { onChatClick(chat.id) }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(48.dp).clip(CircleShape).background(Green100), contentAlignment = Alignment.Center) { Text("👤", fontSize = 24.sp) }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text(chat.otherUserName, fontWeight = FontWeight.SemiBold); Text(chat.lastMessage, maxLines = 1, overflow = TextOverflow.Ellipsis, color = TextSecondary, fontSize = 13.sp) }
                        if (chat.unreadCount > 0) Badge(containerColor = PrimaryGreen) { Text("${chat.unreadCount}") }
                    }
                }
            }
        }
    }
}
