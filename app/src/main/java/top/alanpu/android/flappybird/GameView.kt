package top.alanpu.android.flappybird

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

/**
 * Custom view to draw the game area.
 */
class GameView : SurfaceView, Runnable, SurfaceHolder.Callback {

    private lateinit var bmBird: Bitmap
    private lateinit var gameThread: Thread
    private lateinit var surfaceHolder: SurfaceHolder
    private var running = false

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
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
    }

    fun resume() {
        running = true
        gameThread = Thread(this)
        gameThread.start()
    }

    fun pause() {
        running = false
        try {
            gameThread.join()
        }
        catch (e: InterruptedException) {

        }
    }

    override fun run() {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var canvas: Canvas

        println("******* In run(), ${holder.surface.isValid}")
        while (running) {
            if (holder.surface.isValid) {
                canvas = surfaceHolder.lockCanvas()
                canvas.save()
                canvas.drawBitmap(bmBird, 0f, 0f, paint)
                canvas.drawRect(0f, 0f, 100f, 100f, paint)
                canvas.restore()
                holder.unlockCanvasAndPost(canvas)
            }
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

    override fun surfaceCreated(holder: SurfaceHolder) {
        println("******* Surface created, ${holder.surface.isValid}")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        println("******* Surface changed, ${holder.surface.isValid}")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        println("******* Surface destroyed, ${holder.surface.isValid}")
    }
}