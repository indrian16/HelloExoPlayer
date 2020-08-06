package io.indrian.helloexoplayer

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player

class PlaybackStateListener(private val playbackStateCallback: PlaybackStateCallback) : Player.EventListener {

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        when (playbackState) {

            ExoPlayer.STATE_BUFFERING -> playbackStateCallback.showLoading()
            ExoPlayer.STATE_READY -> playbackStateCallback.hideLoading()

            else -> playbackStateCallback.hideLoading()
        }

    }

    interface PlaybackStateCallback {

        fun showLoading()
        fun hideLoading()
    }
}