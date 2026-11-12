package com.ivan.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.ivan.playlistmaker.App
import com.ivan.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.player.data.PlayerRepositoryImpl
import com.ivan.playlistmaker.player.domain.api.PlayerInteractor
import com.ivan.playlistmaker.player.domain.api.PlayerRepository
import com.ivan.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.ivan.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.ivan.playlistmaker.search.data.TracksRepositoryImpl
import com.ivan.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.ivan.playlistmaker.search.domain.api.SearchHistoryRepository
import com.ivan.playlistmaker.search.domain.api.TracksInteractor
import com.ivan.playlistmaker.search.domain.api.TracksRepository
import com.ivan.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.ivan.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.ivan.playlistmaker.settings.data.SettingsRepositoryImpl
import com.ivan.playlistmaker.settings.domain.api.SettingsInteractor
import com.ivan.playlistmaker.settings.domain.api.SettingsRepository
import com.ivan.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.ivan.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.ivan.playlistmaker.sharing.domain.api.ExternalNavigator
import com.ivan.playlistmaker.sharing.domain.api.SharingInteractor
import com.ivan.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {
    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl()
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
        return SearchHistoryRepositoryImpl(sharedPrefs)
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
        return SettingsRepositoryImpl(sharedPrefs, App.DARK_THEME)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            externalNavigator = getExternalNavigator(context),
            shareAppString = context.getString(R.string.practicum_link),
            termsLinkString = context.getString(R.string.terms_link),
            emailString = context.getString(R.string.email),
            emailSubjectString = context.getString(R.string.theme_of_mail),
            emailMessageString = context.getString(R.string.message)
        )
    }
}

