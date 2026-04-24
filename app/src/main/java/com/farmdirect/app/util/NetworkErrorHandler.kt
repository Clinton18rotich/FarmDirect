package com.farmdirect.app.util

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.farmdirect.app.ui.theme.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorHandler(private val context: Context) {
    
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is UnknownHostException -> "No internet connection. Please check your network."
            is SocketTimeoutException -> "Connection timed out. Please try again."
            is ConnectException -> "Cannot connect to server. Please try later."
            else -> throwable.message ?: "Something went wrong. Please try again."
        }
    }
    
    fun getRetryAction(throwable: Throwable, onRetry: () -> Unit): () -> Unit {
        return when (throwable) {
            is UnknownHostException -> {
                { context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS)) }
            }
            else -> onRetry
        }
    }
    
    fun isNetworkError(throwable: Throwable): Boolean {
        return throwable is UnknownHostException ||
               throwable is SocketTimeoutException ||
               throwable is ConnectException
    }
}

@Composable
fun NetworkErrorView(
    message: String,
    onRetry: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(Icons.Default.WifiOff, null, tint = StatusError, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text("Connection Error", fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Text(message, color = TextSecondary, textAlign = TextAlign.Center, lineHeight = 22.sp)
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onOpenSettings,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Settings, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Open Settings")
                }
                Button(
                    onClick = onRetry,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
                ) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Retry")
                }
            }
        }
    }
}
