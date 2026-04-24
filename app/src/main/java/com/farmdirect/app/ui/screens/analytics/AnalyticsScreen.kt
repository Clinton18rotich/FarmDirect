package com.farmdirect.app.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(onBack: () -> Unit, viewModel: AnalyticsViewModel = hiltViewModel()) {
    val analytics by viewModel.analytics.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Farm Analytics", color = TextWhite) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } },
                actions = { IconButton(onClick = { }) { Icon(Icons.Default.Share, "Share", tint = TextWhite) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)
            )
        },
        containerColor = BackgroundLight
    ) { padding ->
        analytics?.let { a ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                // Key Metrics
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("💰 Revenue", "KES ${"%,.0f".format(a.totalRevenue)}", "+${a.revenueGrowth}%", PrimaryGreen, Modifier.weight(1f))
                    MetricCard("📦 Orders", "${a.totalOrders}", "+${a.orderGrowth}%", AccentGold, Modifier.weight(1f))
                }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard("⭐ Rating", "${a.averageRating}", "Top 5%", StatusSuccess, Modifier.weight(1f))
                    MetricCard("🔄 Repeat", "${a.repeatBuyers}", "Buyers", SkyBlue, Modifier.weight(1f))
                }
                
                Spacer(Modifier.height(20.dp))
                
                // Best Selling
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("🏆 Best Selling", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        a.cropBreakdown.forEach { crop ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(12.dp).clip(CircleShape).background(Color(android.graphics.Color.parseColor(crop.color))))
                                Spacer(Modifier.width(8.dp))
                                Text(crop.cropName, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
                                Text("KES ${"%,.0f".format(crop.revenue)}", fontWeight = FontWeight.SemiBold, color = PrimaryGreen)
                                Spacer(Modifier.width(8.dp))
                                Text("${crop.percentage}%", color = TextSecondary, fontSize = 13.sp)
                            }
                            LinearProgressIndicator(
                                progress = (crop.percentage / 100).toFloat(),
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                color = Color(android.graphics.Color.parseColor(crop.color)),
                                trackColor = Green50
                            )
                        }
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Monthly Sales Trend
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("📈 Monthly Sales", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(16.dp))
                        a.monthlySales.forEach { sale ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(sale.month, modifier = Modifier.width(40.dp), fontSize = 13.sp, color = TextSecondary)
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(20.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Green50)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth((sale.revenue / a.totalRevenue).toFloat().coerceIn(0.05f, 1f))
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(PrimaryGreen)
                                    )
                                }
                                Spacer(Modifier.width(8.dp))
                                Text("KES ${"%,.0f".format(sale.revenue)}", fontSize = 13.sp, fontWeight = FontWeight.Medium, modifier = Modifier.width(80.dp))
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(16.dp))
                
                // Top Buyers
                Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Column(Modifier.padding(20.dp)) {
                        Text("🤝 Top Buyers", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(12.dp))
                        a.topBuyers.forEachIndexed { index, buyer ->
                            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(Modifier.size(36.dp).clip(CircleShape).background(PrimaryGreen), contentAlignment = Alignment.Center) {
                                    Text("${index + 1}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(buyer.name, fontWeight = FontWeight.Medium)
                                    Text("${buyer.orders} orders", color = TextSecondary, fontSize = 12.sp)
                                }
                                Text("KES ${"%,.0f".format(buyer.totalSpent)}", fontWeight = FontWeight.Bold, color = PrimaryGreen)
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, subtitle: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, color = TextSecondary, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = color)
            Text(subtitle, color = color.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}
