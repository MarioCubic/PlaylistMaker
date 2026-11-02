package com.ivan.playlistmaker.search.domain.api

import com.ivan.playlistmaker.data.dto.TrackResponse

interface TracksInteractor {
    fun searchTracks(
        expression: String,
        onResult: (TrackResponse) -> Unit,
        onError: () -> Unit,
        onNoInternet: () -> Unit
    )
}
