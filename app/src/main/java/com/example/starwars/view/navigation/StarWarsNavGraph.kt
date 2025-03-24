package com.example.starwars.view.navigation

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starwars.R
import com.example.starwars.data.CharacterData
import com.example.starwars.view.screens.CharacterDetailScreen
import com.example.starwars.view.screens.HomeScreen
import dagger.hilt.android.qualifiers.ApplicationContext

enum class StarWarsScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    CharacterDetails(title = R.string.character_details)
}

@Composable
fun StarWarsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = StarWarsScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = StarWarsScreen.Home.name) {
            HomeScreen(
                navigateToCharacterDetails = { id ->
                    navController.navigate(route = "${StarWarsScreen.CharacterDetails.name}/$id")
                }
            )
        }

        composable(route = "${StarWarsScreen.CharacterDetails.name}/{id}") {
            val id = navController.currentBackStackEntry?.arguments?.getString("id")?.toInt()
            if (id != null && navController.previousBackStackEntry != null) {
                CharacterDetailScreen(
                    id = id,
                    navigateBack = { navController.navigateUp() },
                    viewModel = hiltViewModel(navController.previousBackStackEntry!!)
                )
            }
        }
    }
}