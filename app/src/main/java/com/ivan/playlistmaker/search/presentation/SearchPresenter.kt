package com.ivan.playlistmaker.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.model.HistoryActions
import com.ivan.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.ivan.playlistmaker.search.domain.api.TracksInteractor

class SearchPresenter(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) {
    private val _screenState = MutableLiveData<SearchScreenState>()
    internal val screenState: LiveData<SearchScreenState> = _screenState

    fun fetchTracks(searchField: String) {
        tracksInteractor.searchTracks(
            searchField,
            onResult = { tracksResponse ->
                _screenState.value = SearchScreenState.DataLoaded(tracksResponse.results)
            },
            onError = { _screenState.value = SearchScreenState.NothingFound },
            onNoInternet = { _screenState.value = SearchScreenState.NetworkError }
        )
    }

    fun saveHistory(track: Track) {
        searchHistoryInteractor.saveHistory(
            track,
            onAddTrack = { trackHistory ->
                _screenState.value = SearchScreenState.AddTrackHistory(trackHistory.toList())
            }
        )
    }

    fun updateHistory(action: HistoryActions) {
        searchHistoryInteractor.clearAndReadHistory(
            action,
            onRead = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory.toList())
            },
            onClear = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory.toList())
            },
            onHide = { trackHistory ->
                _screenState.value = SearchScreenState.SearchHistory(trackHistory.toList())
            }
        )
    }
}
