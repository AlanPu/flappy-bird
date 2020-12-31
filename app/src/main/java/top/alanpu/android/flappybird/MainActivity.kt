package top.alanpu.android.flappybird

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import top.alanpu.android.flappybird.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var themeMedia: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        themeMedia = MediaPlayer.create(this, R.raw.main_theme).let {
            it.isLooping = true
            it!!
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onResume() {
        super.onResume()
        themeMedia.start()
    }

    override fun onPause() {
        super.onPause()
        themeMedia.apply {
            pause()
            seekTo(0)
        }
    }

    override fun onDestroy() {
        themeMedia.apply {
            stop()
            reset()
            release()
        }
        super.onDestroy()
    }
}