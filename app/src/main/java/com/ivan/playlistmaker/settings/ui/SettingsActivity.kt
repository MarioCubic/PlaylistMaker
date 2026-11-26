package com.ivan.playlistmaker.settings.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ivan.playlistmaker.App
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.creator.Creator
import com.ivan.playlistmaker.databinding.ActivitySettingsBinding
import com.ivan.playlistmaker.settings.presentation.SettingsViewModel
import com.ivan.playlistmaker.settings.presentation.SettingsViewModelFactory

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val settingsViewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            Creator.provideSharingInteractor(this),
            Creator.provideSettingsInteractor(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupListeners()

    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.themeSwitcher.isChecked = settingsViewModel.getThemeSettings()
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.updateThemeSetting(checked)
            (applicationContext as App).darkTheme = checked
        }

        binding.header.setNavigationOnClickListener {
            finish()
        }

        binding.shareApp.setOnClickListener {
            settingsViewModel.shareApp()
        }

        binding.support.setOnClickListener {
            settingsViewModel.openSupport()
        }

        binding.terms.setOnClickListener {
            settingsViewModel.openTerms()
        }

    }
}
