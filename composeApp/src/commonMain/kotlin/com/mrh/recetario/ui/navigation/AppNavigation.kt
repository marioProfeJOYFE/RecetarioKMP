package com.mrh.recetario.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mrh.recetario.ui.screens.CreateRecipeScreen
import com.mrh.recetario.ui.screens.DetailScreen
import com.mrh.recetario.ui.screens.HomeScreen
import com.mrh.recetario.ui.screens.VaultScreen

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: Any,
)

val bottomNavItems = listOf(
    BottomNavItem("Explorar", Icons.Default.Search, Home),
    BottomNavItem("Crear", Icons.Default.Add, CreateRecipe),
    BottomNavItem("Favoritos", Icons.Default.Favorite, Vault),
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination != null && bottomNavItems.any { item ->
        currentDestination.hasRoute(item.route::class)
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.hasRoute(item.route::class) == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Home> {
                HomeScreen(
                    onRecipeClick = { mealId -> navController.navigate(Detail(mealId)) },
                )
            }
            composable<CreateRecipe> {
                CreateRecipeScreen(
                    onRecipeSaved = {
                        navController.navigate(Vault) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
            composable<Vault> {
                VaultScreen(
                    onRecipeClick = { mealId -> navController.navigate(Detail(mealId)) },
                )
            }
            composable<Detail> { backStackEntry ->
                val detail = backStackEntry.toRoute<Detail>()
                DetailScreen(
                    mealId = detail.mealId,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
