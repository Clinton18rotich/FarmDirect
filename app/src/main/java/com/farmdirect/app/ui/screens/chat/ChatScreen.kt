package com.farmdirect.app.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatId: String, onBack: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {
    val messages by viewModel.messages.collectAsState()
    var newMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(chatId) { viewModel.loadMessages(chatId) }
    LaunchedEffect(messages.size) { if (messages.isNotEmpty()) scope.launch { listState.animateScrollToItem(messages.size - 1) } }

    Scaffold(topBar = { TopAppBar(title = { Text("Chat", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) },
        bottomBar = { Surface(Modifier.fillMaxWidth(), shadowElevation = 8.dp, color = SurfaceWhite) { Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) { OutlinedTextField(value = newMessage, onValueChange = { newMessage = it }, placeholder = { Text("Message...") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(24.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)); Spacer(Modifier.width(8.dp)); IconButton(onClick = { if (newMessage.isNotBlank()) { viewModel.sendMessage(chatId, newMessage); newMessage = "" } }, modifier = Modifier.background(PrimaryGreen, CircleShape)) { Icon(Icons.Default.Send, null, tint = TextWhite) } } } }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), state = listState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages) { msg ->
                val isMe = msg.senderId == "current_user"
                Box(Modifier.fillMaxWidth(), contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart) {
                    Surface(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = if (isMe) 16.dp else 4.dp, bottomEnd = if (isMe) 4.dp else 16.dp), color = if (isMe) PrimaryGreen else Green50, modifier = Modifier.widthIn(max = 300.dp)) {
                        Text(msg.content, modifier = Modifier.padding(12.dp), color = if (isMe) TextWhite else TextPrimary)
                    }
                }
            }
        }
    }
}
