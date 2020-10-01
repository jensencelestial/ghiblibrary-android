package com.jensencelestial.ghiblibrary.android

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.jensencelestial.ghiblibrary.android.util.logging.TimberReleaseTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GhibLibraryApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(TimberReleaseTree())
        }
    }
}