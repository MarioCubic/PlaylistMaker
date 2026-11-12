package com.ivan.playlistmaker.player.domain.impl

import com.ivan.playlistmaker.player.domain.api.PlayerInteractor
import com.ivan.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun prepareTrack(url: String, onPrepared: () -> Unit) {
        repository.preparePlayer(url, onPrepared)
    }

    override fun playTrack(onPlay: () -> Unit) {
        repository.startPlayer(onPlay)
    }

    override fun pauseTrack(onPause: () -> Unit) {
        repository.pausePlayer(onPause)
    }

    override fun releaseTrack(onRelease: () -> Unit) {
        repository.releaseTrack(onRelease)
    }

    override fun currentPosition(): Int {
        return repository.currentPosition()
    }
}
