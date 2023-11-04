package com.piwew.jetprogrammingapp

import androidx.navigation.NavController
import org.junit.Assert

fun NavController.assertCurrentRouteName(expectedRoutedName: String) {
    Assert.assertEquals(expectedRoutedName, currentBackStackEntry?.destination?.route)
}