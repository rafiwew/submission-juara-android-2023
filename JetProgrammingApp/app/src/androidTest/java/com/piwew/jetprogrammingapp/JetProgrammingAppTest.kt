package com.piwew.jetprogrammingapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.piwew.jetprogrammingapp.model.LanguagesData
import com.piwew.jetprogrammingapp.navigation.Screen
import com.piwew.jetprogrammingapp.ui.theme.JetProgrammingAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class JetProgrammingAppTest {
    @get: Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            JetProgrammingAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                JetProgrammingApp(navController = navController)
            }
        }
    }

    @Test
    fun navHostVerifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHostClickItemNavigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("LanguageList").performScrollToIndex(1)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailLanguage.route)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).assertIsDisplayed()
    }

    @Test
    fun navHostClickItemNavigatesBack() {
        composeTestRule.onNodeWithTag("LanguageList").performScrollToIndex(1)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailLanguage.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHostClickFavoriteAndAboutIconButton() {
        composeTestRule.onNodeWithContentDescription("favorite_page").performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithContentDescription("about_page").performClick()
        navController.assertCurrentRouteName(Screen.About.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @Test
    fun navHostFavoriteClickItemNavigatesToDetailWithData() {
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailLanguage.route)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.add_favorite))
            .performClick()
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithContentDescription("favorite_page").performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).performClick()
        navController.assertCurrentRouteName(Screen.DetailLanguage.route)
        composeTestRule.onNodeWithText(LanguagesData.language[1].name).assertIsDisplayed()
    }
}