package com.farmdirect.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.farmdirect.app.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit, viewModel: AuthViewModel = hiltViewModel()) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.loginState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(PrimaryGreen, PrimaryDarkGreen)))) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text("🌾", fontSize = 60.sp)
            Text("Welcome Back!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextWhite)
            Spacer(Modifier.height(24.dp))
            Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = SurfaceWhite), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(24.dp)) {
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone") }, leadingIcon = { Text("📱") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, leadingIcon = { Text("🔒") }, visualTransformation = PasswordVisualTransformation(), singleLine = true, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen))
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { viewModel.login(phone, password, onLoginSuccess) }, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen), enabled = !state.isLoading) {
                        if (state.isLoading) CircularProgressIndicator(color = TextWhite, modifier = Modifier.size(24.dp)) else Text("Sign In", fontSize = 16.sp)
                    }
                    state.error?.let { Text(it, color = StatusError, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp)) }
                }
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToRegister) { Text("Don't have an account? Sign Up", color = AccentGold) }
        }
    }
}
