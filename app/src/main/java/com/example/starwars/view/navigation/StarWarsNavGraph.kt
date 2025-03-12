package com.example.starwars.view.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starwars.R
import com.example.starwars.data.CharacterData
import com.example.starwars.view.screens.CharacterDetailScreen
import com.example.starwars.view.screens.HomeScreen

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
            HomeScreen(
                navigateToCharacterDetails = { character ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("character", character)
                    navController.navigate("characterDetails")
                }
            )
        }

        composable(route = "characterDetails") {
            val context = LocalContext.current
            val character = navController.previousBackStackEntry?.savedStateHandle?.get<CharacterData>("character")
            if (character != null) {
                CharacterDetailScreen(
                    character = character,
                    navigateBack = { navController.navigateUp() },
                    shareDetails = { shareDetails(context, character) }
                )
            }
        }
    }
}

private fun shareDetails(context: Context, characterData: CharacterData?) {
    if (characterData == null) return
    val shareText =
        """
            |Name: ${characterData.name}
            |Height: ${characterData.height}cm
            |Gender: ${characterData.gender}
            |Birth Year: ${characterData.birth_year}
            |Eye Color: ${characterData.eye_color}
            |Hair Color: ${characterData.hair_color}
        """.trimMargin()
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/*"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(
        Intent.createChooser(
            shareIntent,
            context.getString(R.string.app_name)
        )
    )
}