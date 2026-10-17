package com.ivan.playlistmaker.player

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Locale

class PlayerPresenter () {
    private val playerService = PlayerService()
    private val _screenState = MutableLiveData<PlayerScreenState>()
    internal val screenState: LiveData<PlayerScreenState> = _screenState


    fun prepareTrack(url: String) {
        _screenState.value = PlayerScreenState.Preparing
        playerService.preparePlayer(url,
            onPrepared = { _screenState.value = PlayerScreenState.Prepared }
        )
    }
    fun playTrack() {
        playerService.startPlayer(
            onPlay = { _screenState.value = PlayerScreenState.OnPlay }
        )
    }
    fun pauseTrack() {
        playerService.pausePlayer(
            onPause = { _screenState.value = PlayerScreenState.OnPause }
        )
    }
    fun releaseTrack() {
        playerService.releaseTrack(
            onRelease = { _screenState.value = PlayerScreenState.OnRelease }
        )
    }

    fun localeTimeFormat() : String{
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerService.currentPosition())
    }

    }



sealed class PlayerScreenState {
    data object OnPlay : PlayerScreenState()
    data object OnPause : PlayerScreenState()
    data object Preparing : PlayerScreenState()
    data object Prepared : PlayerScreenState()
    data object OnRelease : PlayerScreenState()

}