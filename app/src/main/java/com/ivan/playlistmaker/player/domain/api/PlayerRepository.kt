package com.ivan.playlistmaker.player.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String, onPrepared: () -> Unit)
    fun startPlayer(onPlay: () -> Unit)
    fun pausePlayer(onPause: () -> Unit)
    fun releaseTrack(onRelease: () -> Unit)
    fun currentPosition(): Int
}
