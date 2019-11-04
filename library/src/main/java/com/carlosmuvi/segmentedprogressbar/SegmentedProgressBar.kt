package com.carlosmuvi.segmentedprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class SegmentedProgressBar : View {

    private var lastCompletedSegment = 0
    private var currentSegmentProgressInPx = 0

    private lateinit var containerRectanglePaint: Paint
    private lateinit var fillRectanglePaint: Paint
    private lateinit var drawingTimer: DrawingTimer
    private lateinit var properties: PropertiesModel

    private var segmentCompletedListener: CompletedSegmentListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet? = null) {
        initDrawingTimer()
        initPropertiesModel(attrs)
        containerRectanglePaint = buildContainerRectanglePaint(properties.containerColor)
        fillRectanglePaint = buildFillRectanglePaint(properties.fillColor)
    }

    private fun initPropertiesModel(attrs: AttributeSet?) {
        properties = PropertiesModel(context, attrs)
    }

    private fun initDrawingTimer() {
        drawingTimer = DrawingTimer()
        drawingTimer.setListener { currentTicks, totalTicks ->
            val segmentWidth = segmentWidth

            currentSegmentProgressInPx = currentTicks * segmentWidth / totalTicks
            if (totalTicks <= currentTicks) {
                lastCompletedSegment++
                currentSegmentProgressInPx = 0
            }
            if (totalTicks == currentTicks) segmentCompletedListener?.onSegmentCompleted(lastCompletedSegment)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawContainerRectangles(canvas)
        drawCompletedRectangles(canvas)
        drawCurrentRectangle(canvas)
    }

    /**
     * Set the color of the unfilled part of the progress bar.
     */
    @Suppress("Unused")
    fun setContainerColor(@ColorInt color: Int) {
        containerRectanglePaint = buildContainerRectanglePaint(color)
    }

    /**
     * Set the color of the filled part of the progress bar.
     */
    @Suppress("Unused")
    fun setFillColor(@ColorInt color: Int) {
        fillRectanglePaint = buildFillRectanglePaint(color)
    }

    /**
     * Set the total number of segments that compose the progress bar.
     */
    @Suppress("Unused")
    fun setSegmentCount(segmentCount: Int) {
        properties.segmentCount = segmentCount
    }

    /**
     * Callback that will be triggered when a segment is filled.
     */
    @Suppress("Unused")
    fun setCompletedSegmentListener(listener: CompletedSegmentListener) {
        this.segmentCompletedListener = listener
    }

    /**
     * Start filling the next unfilled segment.
     * @param timeInMilliseconds total time that will take for the segment to fill.
     */
    fun playSegment(timeInMilliseconds: Long) {
        if (!drawingTimer.isRunning) {
            drawingTimer.start(timeInMilliseconds)
        }
    }

    /**
     * Equivalent to [playSegment], but without animation.
     */
    fun incrementCompletedSegments() {
        if (lastCompletedSegment <= properties.segmentCount) {
            currentSegmentProgressInPx = 0
            drawingTimer.reset()
            lastCompletedSegment++
            invalidate()
            segmentCompletedListener?.onSegmentCompleted(lastCompletedSegment)
        }
    }

    /**
     * If segment filling playing, pauses the progress.
     */
    fun pause() = drawingTimer.pause()

    /**
     * If segment filling paused, resumes the progress.
     */
    fun resume() = drawingTimer.resume()

    /**
     * Resets the current bar state, clearing all the segments.
     */
    fun reset() = setCompletedSegments(0)

    /**
     * Checks if the current progress bar filling is paused.
     */
    fun isPaused() = drawingTimer.isPaused

    /**
     * Directly the given number of [completedSegments].
     */
    fun setCompletedSegments(completedSegments: Int) {
        if (completedSegments <= properties.segmentCount) {
            currentSegmentProgressInPx = 0
            drawingTimer.reset()
            lastCompletedSegment = completedSegments
            invalidate()
        }
    }

    /*
      PRIVATE METHODS
       */
    private fun drawContainerRectangles(canvas: Canvas) {
        val segmentWidth = segmentWidth

        var leftX = 0
        var rightX = leftX + segmentWidth
        val topY = 0
        val botY = height

        for (i in 0 until properties.segmentCount) {
            drawRoundedRect(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(),
                    containerRectanglePaint)
            leftX += segmentWidth + properties.segmentGapWidth
            rightX = leftX + segmentWidth
        }
    }

    private fun drawCompletedRectangles(canvas: Canvas) {
        val segmentWidth = segmentWidth

        var leftX = 0
        var rightX = leftX + segmentWidth
        val topY = 0
        val botY = height

        for (i in 0 until lastCompletedSegment) {
            drawRoundedRect(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(), fillRectanglePaint)
            leftX += segmentWidth + properties.segmentGapWidth
            rightX = leftX + segmentWidth
        }
    }

    private fun drawCurrentRectangle(canvas: Canvas) {
        val segmentWidth = segmentWidth

        val leftX = lastCompletedSegment * (segmentWidth + properties.segmentGapWidth)
        val rightX = leftX + currentSegmentProgressInPx
        val topY = 0
        val botY = height
        drawRoundedRect(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(), fillRectanglePaint)
    }

    private fun drawRoundedRect(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        paint: Paint
    ) {

        val path = Path()
        var rx = properties.cornerRadius.toFloat()
        if (rx < 0) rx = 0f
        var ry = 6f
        if (ry < 0) ry = 0f
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        with(path) {
            moveTo(right, top + ry)
            rQuadTo(0f, -ry, -rx, -ry) // top-right corner
            rLineTo(-widthMinusCorners, 0f)
            rQuadTo(-rx, 0f, -rx, ry) // top-left corner
            rLineTo(0f, heightMinusCorners)

            rQuadTo(0f, ry, rx, ry) // bottom-left corner
            rLineTo(widthMinusCorners, 0f)
            rQuadTo(rx, 0f, rx, -ry) // bottom-right corner

            rLineTo(0f, -heightMinusCorners)

            close()
        }

        canvas.drawPath(path, paint)
    }

    private fun buildFillRectanglePaint(@ColorInt color: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        return paint
    }

    private fun buildContainerRectanglePaint(@ColorInt color: Int): Paint {
        val paint = Paint()
        paint.color = color
        paint.style = Paint.Style.FILL
        return paint
    }

    private val segmentWidth: Int
        get() = width / properties.segmentCount - properties.segmentGapWidth
}

interface CompletedSegmentListener {
    fun onSegmentCompleted(segmentCount: Int)
}
