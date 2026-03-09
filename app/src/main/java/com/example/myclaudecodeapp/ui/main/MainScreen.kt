package com.example.myclaudecodeapp.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myclaudecodeapp.ui.cart.CartScreen
import com.example.myclaudecodeapp.ui.home.HomeScreen
import com.example.myclaudecodeapp.ui.settings.SettingsScreen

/** Bottom Navigationの各タブ定義 */
sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    data object Home : BottomNavItem(
        route = "bottom_home",
        icon = Icons.Filled.Home,
        contentDescription = "ホーム"
    )
    data object Cart : BottomNavItem(
        route = "bottom_cart",
        icon = Icons.Filled.ShoppingCart,
        contentDescription = "カート"
    )
    data object Settings : BottomNavItem(
        route = "bottom_settings",
        icon = Icons.Filled.Settings,
        contentDescription = "設定"
    )
}

private val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Cart,
    BottomNavItem.Settings
)

/**
 * Bottom Navigationを持つメイン画面のComposable
 */
@Composable
fun MainScreen(id: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                // バックスタックを積み重ねず、スタートDestinationに戻す
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen() }
            composable(BottomNavItem.Cart.route) { CartScreen() }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
        }
    }
}
