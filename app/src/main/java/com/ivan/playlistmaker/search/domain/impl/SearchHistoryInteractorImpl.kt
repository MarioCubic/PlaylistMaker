package com.ivan.playlistmaker.search.domain.impl

import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.model.HistoryActions
import com.ivan.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.ivan.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {
    override fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit) {
        repository.saveHistory(track, onAddTrack)
    }

    override fun clearAndReadHistory(
        action: HistoryActions,
        onRead: (ArrayDeque<Track>) -> Unit,
        onClear: (ArrayDeque<Track>) -> Unit,
        onHide: (ArrayDeque<Track>) -> Unit
    ) {
        repository.clearAndReadHistory(action, onRead, onClear, onHide)
    }
}
