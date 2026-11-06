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
    // private val searchHistory = ArrayDeque<Track>()

    override fun getHistory(): ArrayDeque<Track>? {
        val json = sharedPreferences?.getString(SEARCH_HISTORY, null)

        if (json != null) {
            val type = object : TypeToken<ArrayDeque<Track>>() {}.type
            return Gson().fromJson(json, type)
        } else return null;
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

// ЛЕГАСИ

/*  override fun saveHistory(track: Track?, onAddTrack: (ArrayDeque<Track>) -> Unit) {
      addTrackToHistory(track)
      writeSharedPrefs(searchHistory)
      onAddTrack(searchHistory)
  }

  private fun addTrackToHistory(track: Track?) {

          if (track != null) {
              searchHistory.addFirst(track)
          }
          writeSharedPrefs(searchHistory)
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

  } */

