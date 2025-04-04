package com.example.starwars.test

import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.starwars.MainActivity
import com.example.starwars.R
import com.example.starwars.view.navigation.StarWarsScreen
import com.example.starwars.view.screens.CharacterDetailScreen
import com.example.starwars.view.screens.HomeScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class StarWarsScreenNavigationTest {
    @get: Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController

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

                composable(route = "${StarWarsScreen.CharacterDetails.name}/{id}") {
                    val id = navController.currentBackStackEntry?.arguments?.getString("id")?.toInt()
                    if (id != null) {
                        CharacterDetailScreen(
                            id = id,
                            navigateBack = { navController.navigateUp() }
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