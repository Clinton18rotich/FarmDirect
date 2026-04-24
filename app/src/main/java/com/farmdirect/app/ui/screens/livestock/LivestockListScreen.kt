package com.farmdirect.app.ui.screens.livestock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LivestockListScreen(onBack: () -> Unit, viewModel: LivestockViewModel = hiltViewModel()) {
    val livestock by viewModel.livestock.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadLivestock() }
    Scaffold(topBar = { TopAppBar(title = { Text("Livestock", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            item { LazyRow(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(listOf("All" to "🐾", "Cattle" to "🐄", "Goats" to "🐐", "Sheep" to "🐑", "Chicken" to "🐔")) { (name, emoji) -> FilterChip(selected = false, onClick = { viewModel.loadLivestock(if (name == "All") null else name.uppercase()) }, label = { Text("$emoji $name") }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryGreen)) } } }
            items(livestock) { l ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp).clickable { }, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(model = l.images.firstOrNull() ?: "", null, Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)), contentScale = ContentScale.Crop)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) { Text("${l.breed} ${l.animalType}", fontWeight = FontWeight.Bold); Text("${l.ageMonths}mo • ${l.weightKg}kg", color = TextSecondary, fontSize = 13.sp); Text("KES ${"%,.0f".format(l.price)}", fontWeight = FontWeight.Bold, color = PrimaryGreen, fontSize = 16.sp) }
                    }
                }
            }
        }
    }
}
