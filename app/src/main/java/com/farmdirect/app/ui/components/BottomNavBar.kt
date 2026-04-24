package com.farmdirect.app.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.farmdirect.app.ui.navigation.bottomNavItems
import com.farmdirect.app.ui.theme.*

@Composable
fun BottomNavBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(containerColor = SurfaceWhite, contentColor = PrimaryGreen) {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                label = { Text(screen.title, style = MaterialTheme.typography.labelMedium) },
                selected = currentRoute == screen.route,
                onClick = { if (currentRoute != screen.route) navController.navigate(screen.route) { popUpTo(navController.graph.startDestinationId) { saveState = true }; launchSingleTop = true; restoreState = true } },
                colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryGreen, selectedTextColor = PrimaryGreen, unselectedIconColor = TextSecondary, unselectedTextColor = TextSecondary, indicatorColor = Green50)
            )
        }
    }
}
