package com.ivan.playlistmaker.search.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ivan.playlistmaker.App.Companion.SEARCH_HISTORY
import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.search.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences?) :
    SearchHistoryRepository {


    override fun getHistory(): ArrayDeque<Track> {
        val json = sharedPreferences?.getString(SEARCH_HISTORY, null)

        if (json != null) {
            val type = object : TypeToken<ArrayDeque<Track>>() {}.type
            return Gson().fromJson(json, type)
        } else return ArrayDeque()
    }


    override fun saveHistory(history: ArrayDeque<Track>) {
        val json = Gson().toJson(history)
        sharedPreferences?.edit {
            putString(SEARCH_HISTORY, json)

        }
    }

    override fun clearHistory() {
        sharedPreferences?.edit {
            remove(SEARCH_HISTORY)
        }
    }
}

