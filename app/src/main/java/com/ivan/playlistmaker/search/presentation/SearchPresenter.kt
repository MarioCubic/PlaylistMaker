package com.ivan.playlistmaker.search.presentation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivan.playlistmaker.model.HistoryActions
import com.ivan.playlistmaker.model.Track
import com.ivan.playlistmaker.search.ApiRepository
import com.ivan.playlistmaker.search.HistoryRepository


class Presenter(sharedPreferences: SharedPreferences? = null) {
    private val apiRepository = ApiRepository()
    private val historyRepository = HistoryRepository(sharedPreferences)
    private val _screenState = MutableLiveData<SearchScreenState>()
    internal val screenState: LiveData<SearchScreenState> = _screenState


    fun fetchTracks(searchField: String) {
        apiRepository.fetchData(
            searchField,
            onResult = { tracksResponse ->
                _screenState.value =
                    SearchScreenState.DataLoaded(tracksResponse.results)
            },
            onError = { _screenState.value = SearchScreenState.NothingFound },
            onNoInternet = { _screenState.value = SearchScreenState.NetworkError }

        )

    }

    fun saveHistory(track: Track) {
        historyRepository.saveHistory(
            track,
            onAddTrack = { trackHistory ->
                _screenState.value = SearchScreenState.AddTrackHistory(trackHistory)
            }

        )

    }

    fun updateHistory(action: HistoryActions) {
        historyRepository.clearAndReadHistory(
            action,
            onRead = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory)
            },
            onClear = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory)
            },
            onHide = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory)
            }
        )
    }
}


sealed class SearchScreenState {
    data class DataLoaded(val data: List<Track>) : SearchScreenState()
    data class AddTrackHistory(val data: List<Track>) : SearchScreenState()
    data class SearchHistory(val data: List<Track>) : SearchScreenState()
    data object NothingFound : SearchScreenState()
    data object NetworkError : SearchScreenState()

}