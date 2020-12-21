package top.alanpu.android.flappybird

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Custom view to draw the game area.
 */
class GameView : SurfaceView, Runnable, SurfaceHolder.Callback {

    private lateinit var bmBird: Bitmap
    private lateinit var gameThread: Thread
    private var running = false

    private var birdPosLeft = 0f
    private var birdPosTop = 0f

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
            context,
            attrs,
            defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        setZOrderOnTop(true)
        keepScreenOn = true
        bmBird = BitmapFactory.decodeResource(resources, R.drawable.ic_bird)
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }

    fun resume() {
        startGame()
    }

    fun pause() {
        stopGame()
    }

    override fun run() {
        while (running) {
            birdPosTop += 5
            draw()
        }

//        with(holder) {
//            this.lockCanvas().let {
//                it.save()
//                it.drawBitmap(bmBird, 0f, 0f, paint)
//                it.drawRect(0f, 0f, 100f, 100f, paint)
//                it.restore()
//                this.unlockCanvasAndPost(it)
//            }
//        }
    }

    private fun draw() {
        if (holder.surface.isValid) {
            holder.apply {
                this.lockCanvas()?.let {
                    // Clear the canvas
                    it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    it.save()
                    it.drawBitmap(bmBird, birdPosLeft, birdPosTop, null)
                    it.restore()
                    this.unlockCanvasAndPost(it)
                }
            }
        }
    }

    private fun stopGame() {
        running = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
        }
    }

    private fun startGame() {
        running = true
        gameThread = Thread(this)
        gameThread.start()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        startGame()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }
}