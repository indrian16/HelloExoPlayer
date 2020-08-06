package io.indrian.helloexoplayer

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity(),
    PlaybackStateListener.PlaybackStateCallback,
    IPlayer {

    private var playbackStateListener = PlaybackStateListener(this)
    private var player: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
    }

    override fun showLoading() {
        progress_circular.toVisible()
    }

    override fun hideLoading() {
        progress_circular.toInvisible()
    }

    override fun initializePlayer() {

        if (player == null) {

            val trackSelector = DefaultTrackSelector(this).apply {
                setParameters(this.buildUponParameters().setMaxVideoSizeSd())
            }
            player = SimpleExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build()
        }

        player_view.player = player

        val uri = Uri.parse(getString(R.string.media_url_dash))
        val mediaSource = buildMediaSource(uri)

        player.run {
            this?.playWhenReady = playWhenReady
            this?.seekTo(currentWindow, playbackPosition)
            this?.addListener(playbackStateListener)
            this?.prepare(mediaSource, false, false)
        }
    }

    override fun releasePlayer() {

        if (player != null) {

            player?.also {
                playbackPosition = it.currentPosition
                currentWindow = it.currentWindowIndex
                playWhenReady = it.playWhenReady
            }.run {
                this?.removeListener(playbackStateListener)
                this?.release()
            }

            player = null
        }

    }

    override fun buildMediaSource(uri: Uri): MediaSource {

        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            "exoplayer-codelab"
        )
        val mediaDataSourceFactory = DashMediaSource.Factory(dataSourceFactory)
        return mediaDataSourceFactory.createMediaSource(uri)
    }

    @SuppressLint("InlinedApi")
    override fun hideSystemUi() {
        player_view.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }
}