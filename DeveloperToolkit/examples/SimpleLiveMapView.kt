package example.livemap

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class SimpleLiveMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private val mapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val playerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(88, 166, 255)
    }

    private var map: Bitmap? = null
    private var state: TrackerSnapshot? = null

    fun showMap(bitmap: Bitmap, snapshot: TrackerSnapshot) {
        map = bitmap
        state = snapshot
        invalidate()
    }

    fun updatePlayer(snapshot: TrackerSnapshot) {
        state = snapshot
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bitmap = map ?: return
        val snapshot = state ?: return

        val scale = min(
            width.toFloat() / bitmap.width.toFloat(),
            height.toFloat() / bitmap.height.toFloat()
        )
        val drawWidth = bitmap.width * scale
        val drawHeight = bitmap.height * scale
        val left = (width - drawWidth) / 2f
        val top = (height - drawHeight) / 2f
        val destination = RectF(left, top, left + drawWidth, top + drawHeight)

        canvas.drawBitmap(bitmap, null, destination, mapPaint)

        val playerX = left + ((snapshot.x + 0.5f) / snapshot.mapWidth) * drawWidth
        val playerY = top + ((snapshot.y + 0.5f) / snapshot.mapHeight) * drawHeight
        val radius = min(drawWidth / snapshot.mapWidth, drawHeight / snapshot.mapHeight) * 0.35f
        canvas.drawCircle(playerX, playerY, radius.coerceAtLeast(5f), playerPaint)
    }
}

