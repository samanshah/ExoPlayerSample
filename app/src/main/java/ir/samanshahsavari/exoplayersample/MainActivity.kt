package ir.samanshahsavari.exoplayersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import ir.samanshahsavari.exoplayersample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Player.Listener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var exoPlayer: ExoPlayer

    private companion object {
        const val BUNDLE_KEY_SEEK_TIME = "SeekTime"
        const val BUNDLE_KEY_MEDIA_ITEM_INDEX = "MediaItemIndex"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupExoPlayer()

        addSampleMediaItems()

        savedInstanceState?.let { bundle ->
            val mediaItemIndex = bundle.getInt(BUNDLE_KEY_MEDIA_ITEM_INDEX)
            val seekTime = bundle.getLong(BUNDLE_KEY_SEEK_TIME)
            exoPlayer.seekTo(mediaItemIndex, seekTime)
            exoPlayer.play()
        }
    }

    private fun addSampleMediaItems() {
        addMediaToExoPlayer(MediaItem.fromUri(getString(R.string.sportNewsVideo)))
        addMediaToExoPlayer(MediaItem.fromUri(getString(R.string.media_url_mp4)))
        addMediaToExoPlayer(MediaItem.fromUri(getString(R.string.test_mp3)))
        addMediaToExoPlayer(MediaItem.fromUri(getString(R.string.myTestMp4)))
    }

    private fun setupExoPlayer() {
        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer.addListener(this)
        binding.exoVideoView.player = exoPlayer
    }

    private fun addMediaToExoPlayer(mediaItem: MediaItem) {
        exoPlayer.addMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putLong(BUNDLE_KEY_SEEK_TIME, exoPlayer.currentPosition)
        outState.putInt(BUNDLE_KEY_MEDIA_ITEM_INDEX, exoPlayer.currentMediaItemIndex)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        binding.pbProgressBar.isVisible = playbackState == Player.STATE_BUFFERING
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)

        binding.tvTitle.text = mediaMetadata.title ?: mediaMetadata.displayTitle ?: "Title not found!"
    }

    override fun onStop() {
        super.onStop()
        exoPlayer.release()
    }
}