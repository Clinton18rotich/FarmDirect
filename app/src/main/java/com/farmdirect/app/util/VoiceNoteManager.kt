package com.farmdirect.app.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*
import java.io.File
import java.io.IOException

class VoiceNoteManager(private val context: Context) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var recordingFile: File? = null
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    
    private val _recordingDuration = MutableStateFlow(0L)
    val recordingDuration = _recordingDuration.asStateFlow()
    
    fun startRecording() {
        recordingFile = File(context.cacheDir, "voice_${System.currentTimeMillis()}.mp3")
        
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(recordingFile!!.absolutePath)
            
            try {
                prepare()
                start()
                _isRecording.value = true
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    
    fun stopRecording(): File? {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            _isRecording.value = false
            return recordingFile
        } catch (e: Exception) {
            return null
        }
    }
    
    fun playRecording(file: File) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(file.absolutePath)
                prepare()
                start()
                _isPlaying.value = true
                setOnCompletionListener {
                    _isPlaying.value = false
                    release()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    
    fun stopPlayback() {
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
    }
    
    fun cancelRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        _isRecording.value = false
        recordingFile?.delete()
    }
    
    fun release() {
        mediaRecorder?.release()
        mediaPlayer?.release()
    }
}

@Composable
fun VoiceNoteRecorder(
    onSend: (File) -> Unit,
    onCancel: () -> Unit
) {
    var isRecording by remember { mutableStateOf(false) }
    var duration by remember { mutableLongStateOf(0L) }
    
    val pulseScale by rememberInfiniteTransition().animateFloat(1f, 1.3f, infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "pulse")
    
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Cancel button
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Delete, "Cancel", tint = StatusError)
        }
        
        // Recording indicator
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(24.dp),
            color = if (isRecording) StatusError.copy(alpha = 0.1f) else Green50
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isRecording) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(StatusError)
                            .scale(pulseScale)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text("Recording... ${formatVoiceDuration(duration)}", fontSize = 14.sp, color = StatusError)
                } else {
                    Text("Tap and hold to record", fontSize = 14.sp, color = TextSecondary)
                }
            }
        }
        
        // Record/Send button
        FloatingActionButton(
            onClick = { },
            containerColor = if (isRecording) StatusError else PrimaryGreen,
            modifier = Modifier.size(48.dp).scale(if (isRecording) pulseScale else 1f)
        ) {
            Icon(
                if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                "Record",
                tint = TextWhite
            )
        }
    }
}

@Composable
fun VoiceNotePlayer(file: File) {
    var isPlaying by remember { mutableStateOf(false) }
    
    Surface(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Green50
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    "Play",
                    tint = PrimaryGreen
                )
            }
            
            // Waveform visualization
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryGreen.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(20) {
                        val height by rememberInfiniteTransition().animateFloat(4f, 20f, infiniteRepeatable(tween(300 + it * 50), RepeatMode.Reverse), label = "bar$it")
                        Box(modifier = Modifier.weight(1f).fillMaxHeight(0.5f + height / 40f).background(PrimaryGreen))
                    }
                }
            }
            
            Text("0:12", fontSize = 12.sp, color = TextSecondary, modifier = Modifier.padding(horizontal = 8.dp))
        }
    }
}

fun formatVoiceDuration(seconds: Long): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%d:%02d".format(mins, secs)
}
