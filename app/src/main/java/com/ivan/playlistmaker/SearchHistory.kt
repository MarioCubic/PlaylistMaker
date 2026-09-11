package com.ivan.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.App.Companion.SEARCH_HISTORY

class SearchHistory(private val sharedPreferences: SharedPreferences) {


    fun addTrackToHistory(track: Track, searchHistory: ArrayDeque<Track>) {
        if (!searchHistory.contains(track)) {
            if (searchHistory.size >= 10) {
                searchHistory.removeLast()
            }
            searchHistory.addFirst(track)
            writeSharedPrefs(searchHistory)
        }else{
            searchHistory.remove(track)
            searchHistory.addFirst(track)
        }
    }

    fun writeSharedPrefs(searchHistory: ArrayDeque<Track>) {
        val json = Gson().toJson(searchHistory)
        sharedPreferences.edit() {
            putString(SEARCH_HISTORY, json)
        }
    }

    fun clearSharedPrefs() {
        sharedPreferences.edit {
            remove(SEARCH_HISTORY)
        }
    }

    fun readSharedPrefs(): ArrayDeque<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null)

        if (json != null) {
            val type = object : TypeToken<ArrayDeque<Track>>() {}.type
            val result: ArrayDeque<Track> = Gson().fromJson(json, type)
            return result
        }

        return ArrayDeque()
    }
}


