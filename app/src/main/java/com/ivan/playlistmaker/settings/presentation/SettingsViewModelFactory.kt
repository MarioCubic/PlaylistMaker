package com.ivan.playlistmaker.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.playlistmaker.settings.domain.api.SettingsInteractor
import com.ivan.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModelFactory (private val sharingInteractor: SharingInteractor, private val settingsInteractor: SettingsInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(sharingInteractor, settingsInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    }
