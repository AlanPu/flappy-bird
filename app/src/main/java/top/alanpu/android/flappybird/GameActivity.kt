package top.alanpu.android.flappybird

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AlertDialog
import top.alanpu.android.flappybird.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity(), GameListener {

//    companion object {
//        const val GAME_OVER = 0x00
//    }

    private lateinit var binding: ActivityGameBinding
    private lateinit var alertDialog: AlertDialog.Builder

//    val handler = Handler(Handler.Callback { message: Message ->
//        when (message.what) {
//            GAME_OVER -> {
//                alertDialog.show()
//                return@Callback true
//            }
//        }
//        return@Callback false
//    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        binding = ActivityGameBinding.inflate(layoutInflater)
        binding.gameView.setGameListener(this)
        setContentView(R.layout.activity_game)

        alertDialog = AlertDialog.Builder(this).apply {
            setTitle(R.string.game_over)
            setMessage(R.string.play_again)
            setCancelable(false)
            setPositiveButton(R.string.yes) { _: DialogInterface, _: Int ->
                {
                    binding.gameView.resetData()
                    binding.gameView.startGame()
                }
            }
            setNegativeButton(R.string.no) { _: DialogInterface, _: Int ->
                {

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.gameView.pause()
    }

    override fun gameOvered() {
        alertDialog.show()
    }
}