package ru.alexey.ndimmatrix.generator.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.alexey.ndimmatrix.generator.ui.screens.ParamsScreen

@Composable
fun Navigator(modifier: Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Params
    ) {
        composable<Home> {

        }

        composable<Params> {
            ParamsScreen()
        }
    }
}