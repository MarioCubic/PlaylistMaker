package com.ivan.playlistmaker.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivan.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.ivan.playlistmaker.search.domain.api.TracksInteractor

class SearchViewModelFactory(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(tracksInteractor, searchHistoryInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}