package com.farmdirect.app.ui.screens.crops

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.domain.model.PostCropRequest
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCropScreen(onBack: () -> Unit, viewModel: CropViewModel = hiltViewModel()) {
    var title by remember { mutableStateOf("") }; var category by remember { mutableStateOf("Maize") }; var variety by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }; var unit by remember { mutableStateOf("kg") }; var pricePerUnit by remember { mutableStateOf("") }
    var quality by remember { mutableStateOf("Grade 1") }; var description by remember { mutableStateOf("") }; var isOrganic by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }; var qualityExpanded by remember { mutableStateOf(false) }; var unitExpanded by remember { mutableStateOf(false) }
    val state by viewModel.postCropState.collectAsState()
    LaunchedEffect(state.isSuccess) { if (state.isSuccess) { viewModel.resetPostState(); onBack() } }

    Scaffold(topBar = { TopAppBar(title = { Text("Post Harvest", color = TextWhite) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = TextWhite) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryGreen)) }, containerColor = BackgroundLight) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") }, leadingIcon = { Text("🌽") }, singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
            Spacer(Modifier.height(10.dp))
            ExposedDropdownMenuBox(expanded = categoryExpanded, onExpandedChange = { categoryExpanded = it }) {
                OutlinedTextField(value = category, onValueChange = {}, readOnly = true, label = { Text("Category") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                ExposedDropdownMenu(expanded = categoryExpanded, onDismissRequest = { categoryExpanded = false }) { listOf("Maize","Beans","Potatoes","Rice","Wheat","Vegetables","Fruits").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { category = it; categoryExpanded = false }) } }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = variety, onValueChange = { variety = it }, label = { Text("Variety") }, singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Qty") }, singleLine = true, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                ExposedDropdownMenuBox(expanded = unitExpanded, onExpandedChange = { unitExpanded = it }, modifier = Modifier.weight(1f)) {
                    OutlinedTextField(value = unit, onValueChange = {}, readOnly = true, label = { Text("Unit") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = unitExpanded) }, modifier = Modifier.menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    ExposedDropdownMenu(expanded = unitExpanded, onDismissRequest = { unitExpanded = false }) { listOf("kg","bag","sack","crate","piece").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { unit = it; unitExpanded = false }) } }
                }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = pricePerUnit, onValueChange = { pricePerUnit = it }, label = { Text("Price/Unit (KES)") }, leadingIcon = { Text("💰") }, singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
            Spacer(Modifier.height(10.dp))
            ExposedDropdownMenuBox(expanded = qualityExpanded, onExpandedChange = { qualityExpanded = it }) {
                OutlinedTextField(value = quality, onValueChange = {}, readOnly = true, label = { Text("Quality") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = qualityExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                ExposedDropdownMenu(expanded = qualityExpanded, onDismissRequest = { qualityExpanded = false }) { listOf("Premium","Grade 1","Grade 2","Grade 3").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { quality = it; qualityExpanded = false }) } }
            }
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, minLines = 2, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
            Spacer(Modifier.height(10.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("🌿 Organic"); Switch(checked = isOrganic, onCheckedChange = { isOrganic = it }, colors = SwitchDefaults.colors(checkedThumbColor = PrimaryGreen)) }
            Spacer(Modifier.height(20.dp))
            Button(onClick = { viewModel.postCrop(PostCropRequest(title,category,variety,quantity.toDoubleOrNull()?:0.0,unit,pricePerUnit.toDoubleOrNull()?:0.0,quality,description,isOrganic,0.0,0.0,"Kitale")) {} }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), enabled = !state.isPosting && title.isNotEmpty() && quantity.isNotEmpty() && pricePerUnit.isNotEmpty()) {
                if (state.isPosting) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp)) else Text("Post Listing", fontSize = 16.sp)
            }
        }
    }
}
