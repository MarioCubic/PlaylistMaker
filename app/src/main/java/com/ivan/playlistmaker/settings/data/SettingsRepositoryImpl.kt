package com.ivan.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.ivan.playlistmaker.settings.domain.api.SettingsRepository
import com.ivan.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val darkThemeKey: String
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPrefs.getBoolean(darkThemeKey, false))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPrefs.edit {
            putBoolean(darkThemeKey, settings.isDarkTheme)
        }
        AppCompatDelegate.setDefaultNightMode(
            if (settings.isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
