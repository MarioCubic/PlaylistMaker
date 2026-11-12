package com.ivan.playlistmaker.search.domain.api

import com.ivan.playlistmaker.data.dto.Track

interface SearchHistoryInteractor {
    fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit)
    fun readHistory(onRead: (ArrayDeque<Track>) -> Unit)
    fun clearHistory(onClear: (ArrayDeque<Track>) -> Unit)
}
