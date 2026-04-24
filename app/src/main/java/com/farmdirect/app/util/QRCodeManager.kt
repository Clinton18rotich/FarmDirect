package com.farmdirect.app.util

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

class QRCodeManager(private val context: Context) {
    
    fun generateQRCode(content: String, width: Int = 500, height: Int = 500): Bitmap? {
        return try {
            val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }
    
    fun generateProductQR(
        productId: String,
        productName: String,
        farmerName: String,
        price: Double,
        location: String
    ): Bitmap? {
        val qrData = """
            FARMDIRECT PRODUCT
            ID: $productId
            Product: $productName
            Farmer: $farmerName
            Price: KES ${"%,.0f".format(price)}
            Location: $location
            Verified by FarmDirect
            Created by Clinton Rotich
        """.trimIndent()
        
        return generateQRCode(qrData)
    }
    
    fun generatePaymentQR(orderId: String, amount: Double): Bitmap? {
        val qrData = "FARMDIRECT|PAY|$orderId|$amount|KES"
        return generateQRCode(qrData)
    }
    
    fun generateReferralQR(referralCode: String): Bitmap? {
        val qrData = "https://farmdirect.africa/ref/$referralCode"
        return generateQRCode(qrData)
    }
}

@Composable
fun QRCodeDisplay(bitmap: Bitmap?, title: String = "Scan this QR Code") {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(16.dp))
            
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(200.dp)
                )
            } else {
                Box(
                    modifier = Modifier.size(200.dp).background(Color.LightGray, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("QR Code\nUnavailable", textAlign = TextAlign.Center, color = Color.DarkGray)
                }
            }
            
            Spacer(Modifier.height(12.dp))
            Text("Point your camera at this code", color = Color.Gray, fontSize = 13.sp)
        }
    }
}
