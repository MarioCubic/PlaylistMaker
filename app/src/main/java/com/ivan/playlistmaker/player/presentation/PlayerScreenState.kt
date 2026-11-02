package com.ivan.playlistmaker.player.presentation

sealed class PlayerScreenState {
    data object OnPlay : PlayerScreenState()
    data object OnPause : PlayerScreenState()
    data object Preparing : PlayerScreenState()
    data object Prepared : PlayerScreenState()
    data object OnRelease : PlayerScreenState()
}