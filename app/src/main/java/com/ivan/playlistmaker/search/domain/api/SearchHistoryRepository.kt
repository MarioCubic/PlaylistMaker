package com.ivan.playlistmaker.search.domain.api

import com.ivan.playlistmaker.data.dto.Track
interface SearchHistoryRepository {
    fun getHistory(): ArrayDeque<Track>?
    fun saveHistory(history: ArrayDeque<Track>)
    fun clearHistory()
}
