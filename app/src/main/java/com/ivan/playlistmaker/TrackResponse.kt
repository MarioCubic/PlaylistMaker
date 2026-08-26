package com.ivan.playlistmaker


class TrackResponse(
    val results: List<RawTrack>
){
data class RawTrack(val trackName: String,
                 val artistName: String,
                 val trackTimeMillis: Long,
                 val artworkUrl100: String)
}


