package com.ivan.playlistmaker.search.domain.impl

import com.ivan.playlistmaker.data.dto.TrackResponse
import com.ivan.playlistmaker.search.domain.api.TracksInteractor
import com.ivan.playlistmaker.search.domain.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    override fun searchTracks(
        expression: String,
        onResult: (TrackResponse) -> Unit,
        onError: () -> Unit,
        onNoInternet: () -> Unit
    ) {
        repository.searchTracks(expression, onResult, onError, onNoInternet)
    }
}
