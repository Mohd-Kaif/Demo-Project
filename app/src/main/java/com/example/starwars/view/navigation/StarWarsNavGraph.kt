package com.example.starwars.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starwars.data.CharacterData
import com.example.starwars.view.screens.CharacterDetailScreen
import com.example.starwars.view.screens.HomeScreen
//import com.example.starwars.viewModel.CharacterDetailViewModel
import com.example.starwars.viewModel.HomeViewModel

@Composable
fun StarWarsNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
//            val viewModel: HomeViewModel = viewModel()
            HomeScreen(
                navigateToCharacterDetails = { character ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("character", character)
                    navController.navigate("characterDetails") },
//                viewModel = viewModel
            )
        }

        composable(route = "characterDetails") {
            val character = navController.previousBackStackEntry?.savedStateHandle?.get<CharacterData>("character")
//            val viewModel: CharacterDetailViewModel = viewModel()
            CharacterDetailScreen(
//                viewModel = viewModel,
                character = character,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}