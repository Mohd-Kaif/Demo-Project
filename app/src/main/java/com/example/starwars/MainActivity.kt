package com.example.starwars

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.starwars.view.screens.HomeScreen
import com.example.starwars.view.theme.StarWarsTheme
import com.example.starwars.viewModel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: HomeViewModel = HomeViewModel()
            StarWarsTheme {
                Log.d("TAG", "start")
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}