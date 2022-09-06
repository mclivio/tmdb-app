package com.donc.tmdbapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TmdbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            // Timber para ahorrar tiempo con los logs. https://www.freecodecamp.org/news/how-to-log-more-efficiently-with-timber-a3f41b193940/
        }
    }
    /*Clase Necesaria para la generacion de codigo de Hilt
    DOC: https://developer.android.com/training/dependency-injection/hilt-android#android-classes */
}