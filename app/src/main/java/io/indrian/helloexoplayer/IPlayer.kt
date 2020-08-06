package io.indrian.helloexoplayer

import android.net.Uri
import com.google.android.exoplayer2.source.MediaSource

interface IPlayer {

    fun initializePlayer()
    fun releasePlayer()
    fun buildMediaSource(uri: Uri): MediaSource
    fun hideSystemUi()
}