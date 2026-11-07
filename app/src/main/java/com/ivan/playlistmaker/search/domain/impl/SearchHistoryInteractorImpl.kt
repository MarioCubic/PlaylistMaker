package com.ivan.playlistmaker.search.domain.impl

import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.ivan.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) : SearchHistoryInteractor {





    override fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit) {

        val history = repository.getHistory()

        if (track != null) {
            if (history.contains(track)) {
                history.remove(track)
            } else if (history.size >= 10) {
                history.removeLast()
            }
            history.addFirst(track)
        }

        repository.saveHistory(history)
        onAddTrack(history)
    }


    override fun readHistory(onRead: (ArrayDeque<Track>) -> Unit) {
        onRead (repository.getHistory())
    }

    override fun clearHistory(onClear: (ArrayDeque<Track>) -> Unit) {
        repository.clearHistory()
        onClear(ArrayDeque())
    }


}
