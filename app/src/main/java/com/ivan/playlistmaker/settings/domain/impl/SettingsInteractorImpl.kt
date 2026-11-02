package com.ivan.playlistmaker.settings.domain.impl

import com.ivan.playlistmaker.settings.domain.api.SettingsInteractor
import com.ivan.playlistmaker.settings.domain.api.SettingsRepository
import com.ivan.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}
