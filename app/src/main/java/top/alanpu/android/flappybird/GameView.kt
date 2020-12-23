package top.alanpu.android.flappybird

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import top.alanpu.android.flappybird.extension.dp
import top.alanpu.android.flappybird.model.Tube
import java.lang.Thread.sleep
import java.nio.channels.Pipe
import java.util.*

/**
 * Custom view to draw the game area.
 */
class GameView : SurfaceView, Runnable, SurfaceHolder.Callback {

    private lateinit var bmBird: Bitmap
    private lateinit var bmTubeUp: Bitmap
    private lateinit var bmTubeDown: Bitmap
    private lateinit var gameThread: Thread

    private val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var score = 0

    private var alive = true
    private var running = false

    private val groundHeight = 425

    private var birdPosX = 0.0f
    private var birdPosY = 0.0f
    private var birdVelocity = 8.0f
    private var birdAcceleration = 0.5f
    private val birdJumpVelocity = -10f

    private val tubeGap = 450
    private val tubeBaseLength = 100
    private var tubeWidth = 0
    private val tubeVelocity = 5
    private val tubeInterval = 300
    private var tubes: MutableList<Tube> = mutableListOf()
    private var tubeCount = 0

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
        bmTubeUp = BitmapFactory.decodeResource(resources, R.drawable.ic_tube1)
        bmTubeDown = BitmapFactory.decodeResource(resources, R.drawable.ic_tube2)

        tubeWidth = bmTubeUp.width

        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)

        scorePaint.apply {
            color = Color.WHITE
            textSize = 32f.dp
        }
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

            for (index in 0..tubeCount) {
                val tube = getTube(index)
                tube.position -= tubeVelocity
                if (tube.position <= -tubeWidth) {
                    tube.position = measuredWidth
                    tube.length = calculateTubeLength()
                }
            }

            draw()

            alive = isAlive()

            if (birdVelocity <= 10.0f) {
                birdVelocity += birdAcceleration
            }
            sleep(15)
        }
    }

    /**
     * Check if the bird is still alive.
     */
    private fun isAlive(): Boolean {

        val birdRightPos = birdPosX + bmBird.width
        val birdBottomPos = birdPosY + bmBird.height

        // Return false if the bird touches the top or bottom
        if (birdPosY <= 5 || birdPosY >= measuredHeight - groundHeight) {
            return false
        }

        for (index in 0..tubeCount) {
            val tube = getTube(index)

            // The tube is in the area between bird and right border of game area
            if (tube.position + tubeWidth >= birdPosX && tube.position < measuredWidth) {
                if (birdRightPos >= tube.position && birdPosX <= tube.position + tubeWidth) {
                    if (birdPosY <= tube.length || birdBottomPos >= tube.length + tubeGap) {
                        return false
                    }
                }
            }
        }

        return true
    }

    /**
     * Calculate a random length for a new tube.
     */
    private fun calculateTubeLength(): Int {
        val baseline = (measuredHeight - tubeGap - groundHeight - tubeBaseLength * 2) / 2
        return (tubeBaseLength + baseline * Random().nextFloat()).toInt() + 10
    }

    /**
     * Handle the touch event.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            birdVelocity = birdJumpVelocity
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

                    it.save()

                    // Draw tubes
                    for (index in 0..tubeCount) {
                        val tube = getTube(index)
                        it.drawBitmap(bmTubeUp,
                                null,
                                Rect(tube.position, 0,
                                        (tube.position + bmTubeUp.width), tube.length),
                                null)
                        it.drawBitmap(bmTubeDown, null,
                                Rect(tube.position, tube.length + tubeGap,
                                        tube.position + bmTubeDown.width, measuredHeight - groundHeight + 65),
                                null)
                    }

                    // Draw the bird
                    it.drawBitmap(bmBird, birdPosX, birdPosY, null)

                    // Draw score
                    it.drawText(score.toString(), 90f, 180f, scorePaint)

                    it.restore()
                    this.unlockCanvasAndPost(it)
                }
            }
        }
    }

    /**
     * Get tube at index.
     */
    private fun getTube(index: Int): Tube {
        if (index >= tubes.size) {
            val tube = Tube(measuredWidth + (tubeWidth + tubeInterval) * index, calculateTubeLength())
            tubes.add(tube)
        }
        return tubes[index]
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
     * When the view size is changed, update the bird's position, and initialize tube list.
     */
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        birdPosX = width.toFloat() / 3
        birdPosY = height.toFloat() / 2 - 400
        tubeCount = (measuredWidth - tubeWidth) / (tubeWidth + tubeInterval)
    }

    /**
     * Stop the game when the view is destroyed.
     */
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGame()
    }
}