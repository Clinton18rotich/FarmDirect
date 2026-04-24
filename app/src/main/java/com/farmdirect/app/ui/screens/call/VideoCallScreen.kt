package com.farmdirect.app.ui.screens.call

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@Composable
fun VideoCallScreen(
    receiverId: String = "",
    receiverName: String = "",
    callType: String = "VIDEO",
    onBack: () -> Unit,
    viewModel: CallViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var callStarted by remember { mutableStateOf(false) }
    
    // Auto-start call if not yet started
    LaunchedEffect(Unit) {
        if (!callStarted && receiverId.isNotEmpty()) {
            viewModel.startCall(receiverId, receiverName, callType)
            callStarted = true
        }
    }
    
    // Incoming call screen
    if (state.isIncomingCall) {
        IncomingCallScreen(
            callerName = state.incomingCall?.callerName ?: "Unknown",
            onAccept = { viewModel.acceptCall() },
            onReject = { viewModel.rejectCall(); onBack() }
        )
        return
    }
    
    // Active call screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Remote video (main view)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Remote video placeholder
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A4A)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("👨‍🌾", fontSize = 80.sp)
                            Spacer(Modifier.height(12.dp))
                            Text(receiverName, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Text(
                                if (state.callState == "CONNECTED") formatDuration(state.callDuration) else state.callState,
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            
            // Local video (picture-in-picture)
            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
                    .size(120.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A5A)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        if (state.isCameraOff) {
                            Icon(Icons.Default.VideocamOff, "Camera Off", tint = Color.White, modifier = Modifier.size(32.dp))
                        } else {
                            Text("👤", fontSize = 40.sp)
                        }
                    }
                }
            }
            
            // Call controls
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xCC1A1A2E),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Mute button
                    CallControlButton(
                        icon = if (state.isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        label = if (state.isMuted) "Unmute" else "Mute",
                        bgColor = if (state.isMuted) StatusError else Color(0xFF3A3A5A),
                        onClick = { viewModel.toggleMute() }
                    )
                    
                    // End call button
                    CallControlButton(
                        icon = Icons.Default.CallEnd,
                        label = "End",
                        bgColor = StatusError,
                        isLarge = true,
                        onClick = { viewModel.endCall(); onBack() }
                    )
                    
                    // Camera toggle
                    CallControlButton(
                        icon = if (state.isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        label = if (state.isCameraOff) "Camera" else "Camera",
                        bgColor = if (state.isCameraOff) StatusWarning else Color(0xFF3A3A5A),
                        onClick = { viewModel.toggleCamera() }
                    )
                }
            }
            
            // Bottom controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CallControlButton(
                    icon = Icons.Default.VolumeUp,
                    label = "Speaker",
                    bgColor = if (state.isSpeakerOn) PrimaryGreen else Color(0xFF3A3A5A),
                    isSmall = true,
                    onClick = { viewModel.toggleSpeaker() }
                )
                CallControlButton(
                    icon = Icons.Default.FlipCameraAndroid,
                    label = "Flip",
                    bgColor = Color(0xFF3A3A5A),
                    isSmall = true,
                    onClick = { viewModel.switchCamera() }
                )
            }
        }
    }
}

@Composable
fun IncomingCallScreen(
    callerName: String,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    val pulseScale by rememberInfiniteTransition().animateFloat(
        1f, 1.1f, infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "pulse"
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1A1A2E), Color(0xFF0F3460)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("👨‍🌾", fontSize = 80.sp)
            Spacer(Modifier.height(16.dp))
            Text(callerName, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Incoming Video Call...", color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
            Spacer(Modifier.height(48.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                // Reject button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = onReject,
                        containerColor = StatusError,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Default.CallEnd, "Reject", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Decline", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
                
                // Accept button
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingActionButton(
                        onClick = onAccept,
                        containerColor = StatusSuccess,
                        modifier = Modifier.size(72.dp).scale(pulseScale)
                    ) {
                        Icon(Icons.Default.Videocam, "Accept", tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Accept", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun CallControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    bgColor: Color,
    isLarge: Boolean = false,
    isSmall: Boolean = false,
    onClick: () -> Unit
) {
    val size = when { isLarge -> 64.dp; isSmall -> 44.dp; else -> 52.dp }
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = bgColor,
            modifier = Modifier.size(size)
        ) {
            Icon(icon, label, tint = Color.White, modifier = Modifier.size(if (isLarge) 28.dp else 22.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, textAlign = TextAlign.Center)
    }
}

fun formatDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
