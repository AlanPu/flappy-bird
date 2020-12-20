package top.alanpu.android.flappybird

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import androidx.annotation.RequiresApi
import top.alanpu.android.flappybird.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}