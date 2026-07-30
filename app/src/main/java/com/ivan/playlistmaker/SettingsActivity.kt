package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<TextView>(R.id.share_app)
        val supportButton = findViewById<TextView>(R.id.support)
        val termsButton = findViewById<TextView>(R.id.terms)

        backButton.setOnClickListener {
            finish()
        }
        shareButton.setOnClickListener {
            val shareIntent = Intent (Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getString(R.string.practicum_link))
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться"))
        }
        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                val adresses: Array <String> = arrayOf(getString(R.string.email))
                putExtra(Intent.EXTRA_EMAIL, adresses)
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.theme_of_mail))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message))

            }
                startActivity(supportIntent)
            }
        termsButton.setOnClickListener {
            val webpage: Uri = getString(R.string.terms_link).toUri()
            val termsIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(termsIntent)
        }

        }


    }

