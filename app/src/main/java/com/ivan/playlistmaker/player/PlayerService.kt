package com.ivan.playlistmaker.player

import android.media.MediaPlayer

class PlayerService {


    private var mediaPlayer = MediaPlayer()

    fun preparePlayer(url: String, onPrepared: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            onPrepared()
        }
    }

    fun startPlayer(onPlay: () -> Unit) {
        mediaPlayer.start()
        onPlay()
    }

    fun pausePlayer(onPause: () -> Unit) {
        mediaPlayer.pause()
        onPause()
    }
    fun releaseTrack(onRelease: () -> Unit) {
        mediaPlayer.release()
        onRelease()
    }
    fun currentPosition() : Int {
        return mediaPlayer.currentPosition
    }
    }

