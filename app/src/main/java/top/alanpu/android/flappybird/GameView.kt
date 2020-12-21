package top.alanpu.android.flappybird

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.lang.Thread.sleep

/**
 * Custom view to draw the game area.
 */
class GameView : SurfaceView, Runnable, SurfaceHolder.Callback {

    private lateinit var bmBird: Bitmap
    private lateinit var gameThread: Thread

    private var alive = true
    private var running = false

    private var birdPosX = 0.0f
    private var birdPosY = 0.0f
    private var birdVelocity = 10.0f
    private var birdAcceleration = 0.4f

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

    /**
     * When the view is resumed, restart the game.
     */
    fun resume() {
        startGame()
    }

    /**
     * When the view is paused, stop the game.
     */
    fun pause() {
        stopGame()
    }

    /**
     * Refresh the UI every 15 ms.
     */
    override fun run() {
        while (running && alive) {
            birdPosY += birdVelocity
            if (birdPosY <= 5 || birdPosY >= measuredHeight - 425) {
                alive = false
            }
            draw()
            if (birdVelocity <= 10.0f) {
                birdVelocity += birdAcceleration
            }
            sleep(15)
        }
    }

    /**
     * Handle the touch event.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            birdVelocity = -13.0f
            return true
        }
        return super.onTouchEvent(event)
    }

    /**
     * Draw the UI.
     */
    private fun draw() {
        if (holder.surface.isValid) {
            holder.apply {
                this.lockCanvas()?.let {

                    // Clear the canvas
                    it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

                    // Draw the bird
                    it.save()
                    it.drawBitmap(bmBird, birdPosX, birdPosY, null)
                    it.restore()
                    this.unlockCanvasAndPost(it)
                }
            }
        }
    }

    /**
     * Stop the game.
     */
    private fun stopGame() {
        running = false
        alive = false
        try {
            gameThread.join()
        } catch (e: InterruptedException) {
        }
    }

    /**
     * Start the game.
     */
    private fun startGame() {
        running = true
        alive = true
        gameThread = Thread(this)
        gameThread.start()
    }

    /**
     * Start the game when the view is created.
     */
    override fun surfaceCreated(holder: SurfaceHolder) {
        startGame()
    }

    /**
     * When the view size is changed, update the bird's position.
     */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        birdPosX = width.toFloat() / 3
        birdPosY = height.toFloat() / 2
    }

    /**
     * Stop the game when the view is destroyed.
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }
}