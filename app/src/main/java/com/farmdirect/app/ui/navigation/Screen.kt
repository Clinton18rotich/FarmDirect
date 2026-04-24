package com.farmdirect.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    data object Splash : Screen("splash")
    data object Login : Screen("login", "Sign In")
    data object Register : Screen("register", "Create Account")
    data object Home : Screen("home", "Home", Icons.Filled.Home)
    data object Search : Screen("search", "Search", Icons.Filled.Search)
    data object PostCrop : Screen("post_crop", "Sell", Icons.Filled.AddCircle)
    data object Chats : Screen("chats", "Messages", Icons.Filled.Chat)
    data object Profile : Screen("profile", "Profile", Icons.Filled.Person)
    data object CropDetail : Screen("crop/{cropId}") { fun createRoute(id: String) = "crop/$id" }
    data object Chat : Screen("chat/{chatId}") { fun createRoute(id: String) = "chat/$id" }
    data object Payment : Screen("payment/{orderId}") { fun createRoute(id: String) = "payment/$id" }
    data object Weather : Screen("weather", "Weather", Icons.Filled.Cloud)
    data object Finance : Screen("finance", "Wallet", Icons.Filled.AccountBalance)
    data object Academy : Screen("academy", "Learn", Icons.Filled.School)
}

val bottomNavItems = listOf(Screen.Home, Screen.Search, Screen.PostCrop, Screen.Chats, Screen.Profile)
