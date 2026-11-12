package com.ivan.playlistmaker.player.presentation

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivan.playlistmaker.player.domain.api.PlayerInteractor
import java.util.Locale

class PlayerPresenter(
    private val playerInteractor: PlayerInteractor
) {
    private val _screenState = MutableLiveData<PlayerScreenState>()
    internal val screenState: LiveData<PlayerScreenState> = _screenState

    fun prepareTrack(url: String) {
        _screenState.value = PlayerScreenState.Preparing
        playerInteractor.prepareTrack(url,
            onPrepared = { _screenState.value = PlayerScreenState.Prepared }
        )
    }

    fun playTrack() {
        playerInteractor.playTrack(
            onPlay = { _screenState.value = PlayerScreenState.OnPlay }
        )
    }

    fun pauseTrack() {
        playerInteractor.pauseTrack(
            onPause = { _screenState.value = PlayerScreenState.OnPause }
        )
    }

    fun releaseTrack() {
        playerInteractor.releaseTrack(
            onRelease = { _screenState.value = PlayerScreenState.OnRelease }
        )
    }

    fun localeTimeFormat(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.currentPosition())
    }
}

