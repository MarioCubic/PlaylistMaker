package com.ivan.playlistmaker.player.domain.api

interface PlayerInteractor {
    fun prepareTrack(url: String, onPrepared: () -> Unit)
    fun playTrack(onPlay: () -> Unit)
    fun pauseTrack(onPause: () -> Unit)
    fun releaseTrack(onRelease: () -> Unit)
    fun currentPosition(): Int
}
