package com.example.starwars.test

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.testing.TestNavHostController
import com.example.starwars.MainActivity
import com.example.starwars.R
import com.example.starwars.StarWarsApp
import com.example.starwars.data.CharacterData
import com.example.starwars.view.navigation.StarWarsScreen
import com.example.starwars.view.screens.CharacterDetailScreen
import com.example.starwars.view.screens.HomeScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class StarWarsScreenNavigationTest {
    @get: Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

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

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Before
    fun setupStarWarsNavHost() {
        composeTestRule.activity.setContent {
            navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = StarWarsScreen.Home.name,
            ) {
                composable(route = StarWarsScreen.Home.name) {
                    val context = LocalContext.current
                    HomeScreen(
                        navigateToCharacterDetails = { character ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(context.getString(
                                R.string.character), character)
                            navController.navigate(StarWarsScreen.CharacterDetails.name)
                        }
                    )
                }

                composable(route = StarWarsScreen.CharacterDetails.name) {
                    val context = LocalContext.current
                    val character = navController.previousBackStackEntry?.savedStateHandle?.get<CharacterData>(context.getString(
                        R.string.character))
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
    }

    @Test
    fun starWarsNavHost_verifyBackNavigationNotShownOnHomeScreen() {
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }

    @Test
    fun starWarsNavHost_verifyShareNavigationNotShownOnHomeScreen() {
        composeTestRule.onNodeWithContentDescription("Share").assertDoesNotExist()
    }
}