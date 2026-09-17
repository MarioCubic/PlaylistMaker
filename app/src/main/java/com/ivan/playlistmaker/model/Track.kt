package com.ivan.playlistmaker.model

import android.icu.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackId: Long
) {
    fun convertTrackTimeToLocale(trackTimeMillis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }
    fun getYear(releaseDate: String): String{
        return releaseDate.substringBefore("-")
    }
}
