package com.ivan.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit



class App : Application() {
    companion object{
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_THEME = "dark_theme"
        const val SEARCH_HISTORY = "search_history"
    }
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit {
            putBoolean(DARK_THEME, darkTheme)
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}

