package com.ivan.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.App.Companion.SEARCH_HISTORY

class HistoryRepository(private val sharedPreferences: SharedPreferences?) {
    private val searchHistory = ArrayDeque<Track>()

    fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit) {
        addTrackToHistory(track)
        writeSharedPrefs(searchHistory)
        onAddTrack(searchHistory)


    }

    fun clearAndReadHistory(
        action: HistoryAction,
        onRead: (ArrayDeque<Track>) -> Unit,
        onClear: (ArrayDeque<Track>) -> Unit,
        onHide: (ArrayDeque<Track>) -> Unit
    ) {
        if (action == HistoryAction.READ) {
            searchHistory.clear()
            searchHistory.addAll(readSharedPrefs())
            onRead(searchHistory)

        }
        if (action == HistoryAction.CLEAR) {
            clearSharedPrefs()
            searchHistory.clear()
            onClear(searchHistory)

        }
        if (action == HistoryAction.HIDE) {
            searchHistory.clear()
            onHide(searchHistory)


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
        sharedPreferences?.edit() {
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
            val result: ArrayDeque<Track> = Gson().fromJson(json, type)
            return result
        }

        return ArrayDeque()
    }
}


