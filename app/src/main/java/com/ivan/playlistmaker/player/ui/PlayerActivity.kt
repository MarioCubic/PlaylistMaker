package com.ivan.playlistmaker.player.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.creator.Creator
import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.databinding.ActivityPlayerBinding
import com.ivan.playlistmaker.player.presentation.PlayerViewModel
import com.ivan.playlistmaker.player.presentation.PlayerScreenState
import com.ivan.playlistmaker.player.presentation.PlayerViewModelFactory

class PlayerActivity : AppCompatActivity() {
    val playerViewModel : PlayerViewModel by viewModels {
        PlayerViewModelFactory(Creator.providePlayerInteractor())
    }
    private lateinit var binding: ActivityPlayerBinding
    val trackTimeHandler = Handler(Looper.getMainLooper())
    var timeThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        observeViewModel()
        setupListeners()
        val json = intent.getStringExtra("track")
        val type = object : TypeToken<Track>() {}.type
        val track: Track = Gson().fromJson(json, type)
        val url = track.previewUrl

        val radius = (CORNER_RADIUS * binding.collectionView.resources.displayMetrics.density).toInt()
        fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast(DELIMITER, REPLACEMENT)
        Glide.with(binding.collectionView)
            .load(getCoverArtwork())
            .placeholder(R.drawable.placeholder_track_312)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(binding.collectionView)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.length.text = track.convertTrackTimeToLocale(track.trackTimeMillis)
        binding.collection.text = viewVisibility(binding.collectionGroup, track.collectionName)
        binding.year.text = viewVisibility(binding.yearGroup, track.getYear(track.releaseDate))
        binding.genre.text = track.primaryGenreName
        binding.country.text = track.country

        binding.trackName.isSelected = true
        binding.artistName.isSelected = true
        if (url != null) {
            playerViewModel.prepareTrack(url)
        }

    }

    private fun setupListeners() {
        val navToolbar = binding.header
        navToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startUpdateTime() {
        if (timeThread?.isAlive == true) return

        timeThread = Thread {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    trackTimeHandler.post {
                        binding.playbackTime.text = playerViewModel.localeTimeFormat()
                    }
                    Thread.sleep(500)
                }
            } catch (_: InterruptedException) {
            }
        }

        timeThread?.start()
    }
    private fun stopUpdateTime() {
        timeThread?.interrupt()
        timeThread = null
    }

    private fun viewVisibility(view: View, data: String): String {
        if (data.isEmpty()) {
            view.visibility = View.GONE
        } else {
            return data
        }
        return ""
    }

    private fun observeViewModel() {
        playerViewModel.screenState.observe(this) { screenState ->
            when (screenState) {
                is PlayerScreenState.Preparing -> {
                    binding.playPauseButton.isEnabled = false
                }

                is PlayerScreenState.Prepared -> {
                    binding.playPauseButton.isEnabled = true
                    binding.playPauseButton.setImageResource(R.drawable.button_play_100)
                    stopUpdateTime()
                    binding.playbackTime.text = CURRENT_TIME_TRACK
                    binding.playPauseButton.setOnClickListener {
                        playerViewModel.playTrack()
                    }
                }

                is PlayerScreenState.OnPlay -> {
                    binding.playPauseButton.setImageResource(R.drawable.button_pause_100)
                    binding.playPauseButton.setOnClickListener {
                        playerViewModel.pauseTrack()
                    }
                    startUpdateTime()
                }

                is PlayerScreenState.OnPause -> {
                    binding.playPauseButton.setImageResource(R.drawable.button_play_100)
                    binding.playPauseButton.setOnClickListener {
                        playerViewModel.playTrack()
                    }
                    stopUpdateTime()
                }
                is PlayerScreenState.OnRelease -> {
                    stopUpdateTime()
                }


                else -> {}

            }
        }
    }
    override fun onPause() {
        super.onPause()
        playerViewModel.pauseTrack()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.releaseTrack()
    }

    companion object {
        const val CORNER_RADIUS = 8
        const val CURRENT_TIME_TRACK = "00:00"
        const val DELIMITER = '/'
        const val REPLACEMENT = "512x512bb.jpg"
    }
}
