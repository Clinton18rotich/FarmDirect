package com.farmdirect.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("FARMER") }
    var county by remember { mutableStateOf("") }
    var userTypeExpanded by remember { mutableStateOf(false) }
    var countyExpanded by remember { mutableStateOf(false) }
    val state by viewModel.loginState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(PrimaryGreen, PrimaryDarkGreen)))) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(40.dp))
            Text("🌱", fontSize = 50.sp)
            Text("Join FarmDirect", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, leadingIcon = { Text("👤") }, singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, leadingIcon = { Text("📱") }, singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, leadingIcon = { Text("🔒") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, leadingIcon = { Text("🔒") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, isError = confirmPassword.isNotEmpty() && password != confirmPassword, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(10.dp))
                    ExposedDropdownMenuBox(expanded = userTypeExpanded, onExpandedChange = { userTypeExpanded = it }) {
                        OutlinedTextField(value = userType, onValueChange = {}, readOnly = true, label = { Text("I am a") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userTypeExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                        ExposedDropdownMenu(expanded = userTypeExpanded, onDismissRequest = { userTypeExpanded = false }) {
                            listOf("FARMER", "BUYER", "RIDER").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { userType = it; userTypeExpanded = false }) }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    ExposedDropdownMenuBox(expanded = countyExpanded, onExpandedChange = { countyExpanded = it }) {
                        OutlinedTextField(value = county, onValueChange = {}, readOnly = true, label = { Text("County") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countyExpanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                        ExposedDropdownMenu(expanded = countyExpanded, onDismissRequest = { countyExpanded = false }) {
                            listOf("Trans Nzoia", "Uasin Gishu", "Nakuru", "Bungoma", "Kakamega").forEach { DropdownMenuItem(text = { Text(it) }, onClick = { county = it; countyExpanded = false }) }
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { viewModel.register(name, phone, password, userType, county, onRegisterSuccess) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), enabled = !state.isLoading && password == confirmPassword) {
                        if (state.isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp)) else Text("Create Account", fontSize = 16.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) { Text("Already have an account? Sign In", color = AccentGold) }
        }
    }
}
