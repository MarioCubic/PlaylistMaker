package com.ivan.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.ivan.playlistmaker.App
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.creator.Creator
import com.ivan.playlistmaker.settings.presentation.SettingsPresenter

class SettingsActivity : AppCompatActivity() {

    private lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        presenter = SettingsPresenter(
            sharingInteractor = Creator.provideSharingInteractor(this),
            settingsInteractor = Creator.provideSettingsInteractor(this)
        )

        val navToolbar = findViewById<MaterialToolbar>(R.id.header)
        val shareButton = findViewById<TextView>(R.id.share_app)
        val supportButton = findViewById<TextView>(R.id.support)
        val termsButton = findViewById<TextView>(R.id.terms)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.themeSwitcher)

        themeSwitcher.isChecked = presenter.getThemeSettings()
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            presenter.updateThemeSetting(checked)
            (applicationContext as App).darkTheme = checked
        }

        navToolbar.setNavigationOnClickListener {
            finish()
        }

        shareButton.setOnClickListener {
            presenter.shareApp()
        }

        supportButton.setOnClickListener {
            presenter.openSupport()
        }

        termsButton.setOnClickListener {
            presenter.openTerms()
        }
    }
}
