package com.ivan.playlistmaker.player

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.model.Track

class PlayerActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val json = intent.getStringExtra("track")
        val type = object : TypeToken<Track>() {}.type
        val track: Track = Gson().fromJson(json, type)

        val navToolbar = findViewById<MaterialToolbar>(R.id.header)
        navToolbar.setNavigationOnClickListener {
            finish()
        }


        val collectionCover = findViewById<ImageView>(R.id.collectionView)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val addToMediaButton = findViewById<ImageView>(R.id.addToPlaylistButton)
        val playPauseButton = findViewById<ImageView>(R.id.playPauseButton)
        val likeButton = findViewById<ImageView>(R.id.likeButton)
        val playbackTime = findViewById<TextView>(R.id.playbackTime)

        val length = findViewById<TextView>(R.id.length)
        val collection = findViewById<TextView>(R.id.collection)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)

        val collectionGroup = findViewById<Group>(R.id.collectionGroup)
        val yearGroup = findViewById<Group>(R.id.yearGroup)


        val radius = (CORNER_RADIUS * collectionCover.resources.displayMetrics.density).toInt()
        fun getCoverArtwork() = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
        Glide.with(collectionCover)
            .load(getCoverArtwork())
            .placeholder(R.drawable.placeholder_track_312)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(collectionCover)

        trackName.text = track.trackName
        artistName.text = track.artistName
        length.text = track.convertTrackTimeToLocale(track.trackTimeMillis)
        collection.text = viewVisibility(collectionGroup, track.collectionName)
        year.text = viewVisibility(yearGroup, track.getYear(track.releaseDate))
        genre.text = track.primaryGenreName
        country.text = track.country

        trackName.isSelected = true
        artistName.isSelected = true

    }

    fun viewVisibility(view: View, data: String): String {
        if (data.isEmpty()) {
            view.visibility = View.GONE
        } else {
            return data
        }
        return ""
    }

companion object{
    const val CORNER_RADIUS = 8
}
}
