package com.piwew.jetprogrammingapp.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object DetailLanguage : Screen("home/{languageId}") {
        fun createRoute(languageId: String) = "home/$languageId"
    }
}
