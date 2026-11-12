package com.ivan.playlistmaker.search.presentation

import com.ivan.playlistmaker.data.dto.Track

sealed class SearchScreenState {
    data class DataLoaded(val data: List<Track>) : SearchScreenState()
    data class AddTrackHistory(val data: List<Track>) : SearchScreenState()
    data class SearchHistory(val data: List<Track>) : SearchScreenState()
    data object NothingFound : SearchScreenState()
    data object NetworkError : SearchScreenState()
}
