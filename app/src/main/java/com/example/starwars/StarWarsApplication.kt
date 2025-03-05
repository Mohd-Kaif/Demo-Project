package com.example.starwars

import android.app.Application
import com.example.starwars.data.AppContainer
import com.example.starwars.data.DefaultContainer

class StarWarsApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultContainer()
    }
}