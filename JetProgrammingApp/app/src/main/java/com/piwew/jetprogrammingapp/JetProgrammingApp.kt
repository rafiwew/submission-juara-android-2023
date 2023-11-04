package com.piwew.jetprogrammingapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.piwew.jetprogrammingapp.navigation.Screen
import com.piwew.jetprogrammingapp.ui.components.TopAppBar
import com.piwew.jetprogrammingapp.ui.screen.about.AboutScreen
import com.piwew.jetprogrammingapp.ui.screen.detail.DetailScreen
import com.piwew.jetprogrammingapp.ui.screen.favorite.FavoriteScreen
import com.piwew.jetprogrammingapp.ui.screen.home.HomeScreen

@Composable
fun JetProgrammingApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier,
        topBar = {
            if (currentRoute != Screen.DetailLanguage.route) {
                if (currentRoute != Screen.Favorite.route) {
                    if (currentRoute != Screen.About.route) {
                        TopAppBar(navController = navController)
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(
                route = Screen.Home.route
            ) {
                HomeScreen(navigateToDetail = { heroId ->
                    navController.navigate(Screen.DetailLanguage.createRoute(heroId))
                })
            }
            composable(
                route = Screen.DetailLanguage.route,
                arguments = listOf(navArgument("languageId") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("languageId") ?: ""
                DetailScreen(
                    languageId = id,
                    navigateBack = { navController.navigateUp() }
                )
            }
            composable(
                route = Screen.Favorite.route
            ) {
                FavoriteScreen(
                    navigateBack = { navController.navigateUp() },
                    navigateToDetail = { languageId ->
                        navController.navigate(Screen.DetailLanguage.createRoute(languageId))
                    }
                )
            }
            composable(
                route = Screen.About.route
            ) {
                AboutScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}