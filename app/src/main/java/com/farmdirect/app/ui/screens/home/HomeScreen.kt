package com.farmdirect.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.farmdirect.app.ui.components.BottomNavBar
import com.farmdirect.app.ui.navigation.Screen
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val crops by viewModel.crops.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories = listOf("All" to "🌾", "Maize" to "🌽", "Beans" to "🫘", "Potatoes" to "🥔", "Vegetables" to "🥬", "Fruits" to "🍌")

    Scaffold(bottomBar = { BottomNavBar(navController = navController) }, containerColor = BackgroundLight) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(PrimaryGreen, PrimaryDarkGreen))).padding(20.dp)) {
                    Column {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text("☀️ Good Morning!", color = TextWhite.copy(alpha = 0.8f)); Text("Find Fresh Produce", color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) }
                            IconButton(onClick = { navController.navigate(Screen.Profile.route) }, modifier = Modifier.clip(CircleShape).background(SurfaceWhite.copy(alpha = 0.2f)).size(48.dp)) { Icon(Icons.Default.Notifications, null, tint = AccentGold) }
                        }
                        Spacer(Modifier.height(16.dp))
                        Surface(modifier = Modifier.fillMaxWidth().height(56.dp).clickable { navController.navigate(Screen.Search.route) }, shape = RoundedCornerShape(14.dp), color = SurfaceWhite) {
                            Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Default.Search, null, tint = TextSecondary); Spacer(Modifier.width(12.dp)); Text("Search crops, livestock...", color = TextSecondary) }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(12.dp)); LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(categories) { (name, emoji) -> FilterChip(selected = selectedCategory == name, onClick = { viewModel.selectCategory(name) }, label = { Text("$emoji $name") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryGreen), shape = RoundedCornerShape(20.dp)) } } }
            if (isLoading) item { Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = PrimaryGreen) } }
            else items(crops) { crop ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { navController.navigate(Screen.CropDetail.createRoute(crop.id)) }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = crop.images.firstOrNull() ?: "", contentDescription = null, modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(crop.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("👨‍🌾 ${crop.farmerName} ⭐ ${crop.farmerRating}", color = TextSecondary, fontSize = 12.sp)
                            Text("📍 ${crop.farmerLocation.take(25)}", color = TextSecondary, fontSize = 12.sp)
                            Text("KES ${"%,.0f".format(crop.totalPrice)}", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 16.sp)
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}
