package com.ivan.playlistmaker

import android.icu.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
) {
    fun convertTrackTimeToLocale(trackTimeMilis: Long): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMilis)
    }
}
