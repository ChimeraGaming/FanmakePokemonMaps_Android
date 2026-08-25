package com.chimeragaming.mapdevkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class LiveMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private val mapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(88, 166, 255)
    }
    private val playerOutline = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private var bitmap: Bitmap? = null
    private var state: TrackerSnapshot? = null

    fun setMap(image: Bitmap, snapshot: TrackerSnapshot) {
        val previous = bitmap
        bitmap = image
        state = snapshot
        invalidate()
        if (previous !== image && previous?.isRecycled == false) previous.recycle()
    }

    fun updateState(snapshot: TrackerSnapshot) {
        state = snapshot
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val image = bitmap ?: return
        val snapshot = state ?: return
        if (width <= 0 || height <= 0) return

        val scale = min(width.toFloat() / image.width, height.toFloat() / image.height)
        val drawWidth = image.width * scale
        val drawHeight = image.height * scale
        val left = (width - drawWidth) / 2f
        val top = (height - drawHeight) / 2f
        val bounds = RectF(left, top, left + drawWidth, top + drawHeight)
        canvas.drawBitmap(image, null, bounds, mapPaint)

        val playerX = left + ((snapshot.playerRealX + 0.5f) / snapshot.mapWidth) * drawWidth
        val playerY = top + ((snapshot.playerRealY + 0.5f) / snapshot.mapHeight) * drawHeight
        val tile = min(drawWidth / snapshot.mapWidth, drawHeight / snapshot.mapHeight)
        val radius = (tile * 0.35f).coerceAtLeast(5f)
        canvas.drawCircle(playerX, playerY, radius, playerPaint)
        canvas.drawCircle(playerX, playerY, radius, playerOutline)
    }

    override fun onDetachedFromWindow() {
        bitmap?.takeIf { !it.isRecycled }?.recycle()
        bitmap = null
        super.onDetachedFromWindow()
    }
}

