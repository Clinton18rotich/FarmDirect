package com.farmdirect.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farmdirect.app.ui.screens.splash.SplashScreen
import com.farmdirect.app.ui.screens.auth.LoginScreen
import com.farmdirect.app.ui.screens.auth.RegisterScreen
import com.farmdirect.app.ui.screens.home.HomeScreen
import com.farmdirect.app.ui.screens.crops.CropDetailScreen
import com.farmdirect.app.ui.screens.crops.PostCropScreen
import com.farmdirect.app.ui.screens.chat.ChatListScreen
import com.farmdirect.app.ui.screens.chat.ChatScreen
import com.farmdirect.app.ui.screens.payment.PaymentScreen
import com.farmdirect.app.ui.screens.profile.ProfileScreen
import com.farmdirect.app.ui.screens.weather.WeatherScreen
import com.farmdirect.app.ui.screens.finance.FinanceDashboardScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } } },
                onNavigateToHome = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Splash.route) { inclusive = true } } }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(0) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Home.route) { popUpTo(0) { inclusive = true } } },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.PostCrop.route) { PostCropScreen(onBack = { navController.popBackStack() }) }
        composable(Screen.Profile.route) { ProfileScreen(onLogout = { navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } } }) }
        composable(Screen.Weather.route) { WeatherScreen() }
        composable(Screen.Finance.route) { FinanceDashboardScreen(onBack = { navController.popBackStack() }) }
        composable(Screen.Chats.route) { ChatListScreen(onChatClick = { id -> navController.navigate(Screen.Chat.createRoute(id)) }) }
        composable(Screen.Chat.route, arguments = listOf(navArgument("chatId") { type = NavType.StringType })) { entry ->
            ChatScreen(chatId = entry.arguments?.getString("chatId") ?: "", onBack = { navController.popBackStack() })
        }
        composable(Screen.CropDetail.route, arguments = listOf(navArgument("cropId") { type = NavType.StringType })) { entry ->
            CropDetailScreen(cropId = entry.arguments?.getString("cropId") ?: "", onBack = { navController.popBackStack() },
                onChatClick = { id -> navController.navigate(Screen.Chat.createRoute(id)) },
                onBuyClick = { id -> navController.navigate(Screen.Payment.createRoute(id)) })
        }
        composable(Screen.Payment.route, arguments = listOf(navArgument("orderId") { type = NavType.StringType })) { entry ->
            PaymentScreen(orderId = entry.arguments?.getString("orderId") ?: "", onBack = { navController.popBackStack() })
        }
    }
}
