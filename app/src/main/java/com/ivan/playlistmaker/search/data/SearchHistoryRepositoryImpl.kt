package com.ivan.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.App.Companion.SEARCH_HISTORY
import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.model.HistoryActions
import com.ivan.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences?) : SearchHistoryRepository {
    private val searchHistory = ArrayDeque<Track>()

    override fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit) {
        addTrackToHistory(track)
        writeSharedPrefs(searchHistory)
        onAddTrack(searchHistory)
    }

    override fun clearAndReadHistory(
        action: HistoryActions,
        onRead: (ArrayDeque<Track>) -> Unit,
        onClear: (ArrayDeque<Track>) -> Unit,
        onHide: (ArrayDeque<Track>) -> Unit
    ) {
        when (action) {
            HistoryActions.READ -> {
                searchHistory.clear()
                searchHistory.addAll(readSharedPrefs())
                onRead(searchHistory)
            }
            HistoryActions.CLEAR -> {
                clearSharedPrefs()
                searchHistory.clear()
                onClear(searchHistory)
            }
            HistoryActions.HIDE -> {
                searchHistory.clear()
                onHide(searchHistory)
            }
        }
    }

    private fun addTrackToHistory(track: Track?) {
        if (!searchHistory.contains(track)) {
            if (searchHistory.size >= 10) {
                searchHistory.removeLast()
            }
            if (track != null) {
                searchHistory.addFirst(track)
            }
            writeSharedPrefs(searchHistory)
        } else {
            searchHistory.remove(track)
            if (track != null) {
                searchHistory.addFirst(track)
            }
        }
    }

    private fun writeSharedPrefs(searchHistory: ArrayDeque<Track>) {
        val json = Gson().toJson(searchHistory)
        sharedPreferences?.edit {
            putString(SEARCH_HISTORY, json)
        }
    }

    private fun clearSharedPrefs() {
        sharedPreferences?.edit {
            remove(SEARCH_HISTORY)
        }
    }

    private fun readSharedPrefs(): ArrayDeque<Track> {
        val json = sharedPreferences?.getString(SEARCH_HISTORY, null)

        if (json != null) {
            val type = object : TypeToken<ArrayDeque<Track>>() {}.type
            return Gson().fromJson(json, type)
        }

        return ArrayDeque()
    }
}
