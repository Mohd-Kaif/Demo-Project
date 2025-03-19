package com.example.starwars.view.navigation

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
            val context = LocalContext.current
            HomeScreen(
                navigateToCharacterDetails = { character ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(context.getString(R.string.character), character)
                    navController.navigate(StarWarsScreen.CharacterDetails.name)
                }
            )
        }

        composable(route = StarWarsScreen.CharacterDetails.name) {
            val context = LocalContext.current
            val character = navController.previousBackStackEntry?.savedStateHandle?.get<CharacterData>(context.getString(R.string.character))
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