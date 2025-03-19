package com.example.starwars.test

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.starwars.MainActivity
import com.example.starwars.StarWarsApp
import com.example.starwars.view.navigation.StarWarsScreen
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

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Before
    fun setupStarWarsNavHost() {
        composeTestRule.activity.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            StarWarsApp(navController = navController)
        }
    }

    @Test
    fun starWarsNavHost_verifyStartDestination() {
        assertEquals(StarWarsScreen.Home.name , navController.currentBackStackEntry?.destination?.route)
    }

    @Test
    fun starWarsNavHost_verifyBackNavigationNotShownOnHomeScreen() {
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()
    }

    // mock data required
//    @Test
//    fun starWarsNavHost_clickOneCharacter_navigatesToCharacterDetailsScreen() {
//        composeTestRule.onNodeWithContentDescription("character")
//            .performClick()
//        assertEquals(StarWarsScreen.CharacterDetails.name, navController.currentBackStackEntry?.destination?.route)
//    }
}