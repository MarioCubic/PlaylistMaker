package com.ivan.playlistmaker.search.domain.api

import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.model.HistoryActions

interface SearchHistoryInteractor {
    fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit)
    fun clearAndReadHistory(
        action: HistoryActions,
        onRead: (ArrayDeque<Track>) -> Unit,
        onClear: (ArrayDeque<Track>) -> Unit,
        onHide: (ArrayDeque<Track>) -> Unit
    )
}
