package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
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
        val shareButton = findViewById<LinearLayout>(R.id.share_app)
        val supportButton = findViewById<LinearLayout>(R.id.support)
        val termsButton = findViewById<LinearLayout>(R.id.terms)

        backButton.setOnClickListener {
            finish()
        }
        shareButton.setOnClickListener {
            val shareIntent = Intent (Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,"https://practicum.yandex.ru/android-developer/?from=main_search_item")
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться"))
        }
        supportButton.setOnClickListener {
            val adresses: Array <String> = arrayOf("mario.cubic@yandex.ru")
            val themeOfMail = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, adresses)
                putExtra(Intent.EXTRA_SUBJECT, themeOfMail)
                putExtra(Intent.EXTRA_TEXT, message)

            }
                startActivity(supportIntent)
            }
        termsButton.setOnClickListener {
            val webpage: Uri = "https://yandex.ru/legal/practicum_offer/ru/".toUri()
            val termsIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(termsIntent)
        }

        }


    }

