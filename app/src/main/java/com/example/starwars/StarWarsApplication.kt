package com.example.starwars

import android.app.Application
import com.example.starwars.data.AppContainer
import com.example.starwars.data.DefaultContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StarWarsApplication: Application()