package com.ivan.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import com.ivan.playlistmaker.settings.domain.api.SettingsInteractor
import com.ivan.playlistmaker.settings.domain.model.ThemeSettings
import com.ivan.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel (
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings().isDarkTheme
    }

    fun updateThemeSetting(isDarkTheme: Boolean) {
        settingsInteractor.updateThemeSetting(ThemeSettings(isDarkTheme))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }
}
