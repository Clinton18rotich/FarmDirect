package com.farmdirect.app.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class LinkPreview(
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val url: String = "",
    val domain: String = ""
)

object RichLinkPreview {
    
    fun extractURL(text: String): String? {
        val urlPattern = Regex("https?://[\\w./?=&%-]+")
        return urlPattern.find(text)?.value
    }
    
    fun generatePreview(url: String): LinkPreview {
        // In production, fetch Open Graph metadata from the URL
        return when {
            url.contains("farmdirect.africa/listings") -> LinkPreview(
                title = "🌾 View Listing on FarmDirect",
                description = "Check out this fresh produce listing on FarmDirect",
                imageUrl = "",
                url = url,
                domain = "farmdirect.africa"
            )
            url.contains("farmdirect.africa/ref") -> LinkPreview(
                title = "🤝 Join FarmDirect",
                description = "Earn KES 100 when you join and complete your first sale!",
                imageUrl = "",
                url = url,
                domain = "farmdirect.africa"
            )
            else -> LinkPreview(
                title = "🔗 Shared Link",
                description = url.take(100),
                url = url,
                domain = url.split("/").getOrNull(2) ?: "unknown"
            )
        }
    }
}

@Composable
fun LinkPreviewCard(preview: LinkPreview, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            if (preview.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = preview.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(preview.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(preview.description, color = TextSecondary, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Text(preview.domain, color = PrimaryGreen, fontSize = 11.sp)
            }
        }
    }
}
