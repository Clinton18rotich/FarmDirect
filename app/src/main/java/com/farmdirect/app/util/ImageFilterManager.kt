package com.farmdirect.app.util

import android.graphics.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ImageFilterManager {
    
    data class CropFilter(
        val name: String,
        val emoji: String,
        val description: String
    )
    
    val filters = listOf(
        CropFilter("Original", "📷", "No filter"),
        CropFilter("Golden Hour", "🌅", "Warm sunlight tones"),
        CropFilter("Fresh Green", "🌿", "Enhances crop colors"),
        CropFilter("Rustic", "🏡", "Farm-style warm tones"),
        CropFilter("Clean White", "✨", "Bright & clean"),
        CropFilter("Vivid", "🎨", "Saturated colors"),
        CropFilter("Vintage", "📸", "Classic film look"),
        CropFilter("Cool Tone", "❄️", "Blue undertones")
    )
    
    fun applyFilter(bitmap: Bitmap, filterName: String): Bitmap {
        return when (filterName) {
            "Golden Hour" -> applyWarmFilter(bitmap, 0.3f)
            "Fresh Green" -> enhanceGreen(bitmap, 1.3f)
            "Rustic" -> applyWarmFilter(bitmap, 0.2f).let { applyVignette(it, 0.3f) }
            "Clean White" -> adjustBrightness(bitmap, 1.2f)
            "Vivid" -> adjustSaturation(bitmap, 1.5f)
            "Vintage" -> applySepia(bitmap)
            "Cool Tone" -> applyCoolFilter(bitmap, 0.2f)
            else -> bitmap
        }
    }
    
    private fun applyWarmFilter(bitmap: Bitmap, intensity: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply { color = Color.argb((intensity * 100).toInt(), 255, 200, 100) }
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        return result
    }
    
    private fun enhanceGreen(bitmap: Bitmap, intensity: Float): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val colorMatrix = ColorMatrix().apply { setScale(1f, intensity, 1f, 1f) }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        Canvas(result).drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun adjustBrightness(bitmap: Bitmap, factor: Float): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val colorMatrix = ColorMatrix().apply { setScale(factor, factor, factor, 1f) }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        Canvas(result).drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun adjustSaturation(bitmap: Bitmap, factor: Float): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(factor)
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        Canvas(result).drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun applySepia(bitmap: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val colorMatrix = ColorMatrix().apply {
            set(floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            ))
        }
        val paint = Paint().apply { colorFilter = ColorMatrixColorFilter(colorMatrix) }
        Canvas(result).drawBitmap(bitmap, 0f, 0f, paint)
        return result
    }
    
    private fun applyVignette(bitmap: Bitmap, intensity: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply { isAntiAlias = true }
        val gradient = RadialGradient(
            bitmap.width / 2f, bitmap.height / 2f, bitmap.width / 1.5f,
            intArrayOf(Color.TRANSPARENT, Color.argb((intensity * 255).toInt(), 0, 0, 0)),
            null, Shader.TileMode.CLAMP
        )
        paint.shader = gradient
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        return result
    }
    
    private fun applyCoolFilter(bitmap: Bitmap, intensity: Float): Bitmap {
        val result = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint().apply { color = Color.argb((intensity * 100).toInt(), 100, 150, 255) }
        canvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        return result
    }
}

@Composable
fun FilterPicker(
    onFilterSelected: (String) -> Unit,
    selectedFilter: String
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ImageFilterManager.filters) { filter ->
            FilterItem(
                filter = filter,
                isSelected = selectedFilter == filter.name,
                onClick = { onFilterSelected(filter.name) }
            )
        }
    }
}

@Composable
fun FilterItem(filter: ImageFilterManager.CropFilter, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick).padding(4.dp)
    ) {
        Surface(
            modifier = Modifier.size(60.dp),
            shape = RoundedCornerShape(12.dp),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(filter.emoji, fontSize = 28.sp)
            }
        }
        Text(filter.name, fontSize = 11.sp, textAlign = TextAlign.Center, modifier = Modifier.width(60.dp))
    }
}
