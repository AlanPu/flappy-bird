package top.alanpu.android.flappybird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import top.alanpu.android.flappybird.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_game)
    }

    override fun onResume() {
        super.onResume()
        binding.gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.gameView.pause()
    }
}