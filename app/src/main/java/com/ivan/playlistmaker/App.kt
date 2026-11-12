package com.ivan.playlistmaker

import android.app.Application
import com.ivan.playlistmaker.creator.Creator

class App : Application() {
    companion object {
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val DARK_THEME = "dark_theme"
        const val SEARCH_HISTORY = "search_history"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val settingsInteractor = Creator.provideSettingsInteractor(this)
        darkTheme = settingsInteractor.getThemeSettings().isDarkTheme
        settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())
    }
}