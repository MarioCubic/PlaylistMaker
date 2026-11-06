package com.ivan.playlistmaker.search.domain.api

import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.model.HistoryActions

interface SearchHistoryInteractor {
    fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit)
    fun getHistory(): List<Track>
    fun clearHistory()
}
