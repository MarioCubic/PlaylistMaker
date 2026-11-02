package com.ivan.playlistmaker.player.data

import android.media.MediaPlayer
import com.ivan.playlistmaker.player.domain.api.PlayerRepository

class PlayerRepositoryImpl : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onPrepared: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onPrepared()
        }
    }

    override fun startPlayer(onPlay: () -> Unit) {
        mediaPlayer.start()
        onPlay()
    }

    override fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause()
        onPause()
    }

    override fun releaseTrack(onRelease: () -> Unit) {
        mediaPlayer.release()
        onRelease()
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}
