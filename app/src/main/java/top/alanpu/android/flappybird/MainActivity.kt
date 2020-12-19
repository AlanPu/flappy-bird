package top.alanpu.android.flappybird

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import top.alanpu.android.flappybird.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    }
}