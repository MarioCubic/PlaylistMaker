package com.ivan.playlistmaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class Presenter {
    private val apiRepository = ApiRepository()
    private val _screenState = MutableLiveData<SearchScreenState>()
    internal val screenState: LiveData<SearchScreenState> = _screenState


    fun fetchTracks(searchField: String){
        _screenState.value = SearchScreenState.SearchHistory
        apiRepository.fetchData(
            searchField,
            onResult = { tracksResponse ->
                _screenState.value =
                    SearchScreenState.DataLoaded(tracksResponse.results)
            },
            onError = { _screenState.value = SearchScreenState.NothingFound },
            onNoInternet = {_screenState.value = SearchScreenState.NetworkError}

        )

        }
    }


sealed class SearchScreenState {
    data class DataLoaded(val data: List<Track>) : SearchScreenState()
    data object SearchHistory : SearchScreenState()
    data object NothingFound : SearchScreenState()
    data object NetworkError : SearchScreenState()
}