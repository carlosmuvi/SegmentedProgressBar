package com.carlosmuvi.segmentedprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.View

/**
 * Created by carlosmuvi on 02/09/16.
 */

class SegmentedProgressBar : View {

  private var lastCompletedSegment = 0
  private var currentSegmentProgressInPx = 0

  private lateinit var containerRectanglePaint: Paint
  private lateinit var fillRectanglePaint: Paint
  private lateinit var drawingTimer: DrawingTimer
  private lateinit var properties: PropertiesModel

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
      invalidate()
    }
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    drawContainerRectangles(canvas)
    drawCompletedRectangles(canvas)
    drawCurrentRectangle(canvas)
  }

  /*
    EXPOSED ATTRIBUTE METHODS
     */

  fun setContainerColor(@ColorInt color: Int) {
    containerRectanglePaint = buildContainerRectanglePaint(color)
  }

  fun setFillColor(@ColorInt color: Int) {
    fillRectanglePaint = buildFillRectanglePaint(color)
  }

  fun setSegmentCount(segmentCount: Int) {
    properties.segmentCount = segmentCount
  }

  /*
    EXPOSED ACTION METHODS
     */

  fun playSegment(timeInMilliseconds: Long) {
    if (!drawingTimer.isRunning) {
      drawingTimer.start(timeInMilliseconds)
    }
  }

  fun pause() {
    drawingTimer.pause()
  }

  fun setCompletedSegments(completedSegments: Int) {
    if (completedSegments <= properties.segmentCount) {
      currentSegmentProgressInPx = 0
      drawingTimer.reset()
      lastCompletedSegment = completedSegments
      invalidate()
    }
  }

  fun incrementCompletedSegments() {
    if (lastCompletedSegment <= properties.segmentCount) {
      currentSegmentProgressInPx = 0
      drawingTimer.reset()
      lastCompletedSegment++
      invalidate()
    }
  }

  fun reset() {
    setCompletedSegments(0)
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

    for (i in 0..properties.segmentCount - 1) {
      drawRoundedRect(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(),
          containerRectanglePaint)
      leftX = leftX + segmentWidth + properties.segmentGapWidth
      rightX = leftX + segmentWidth
    }
  }

  private fun drawCompletedRectangles(canvas: Canvas) {
    val segmentWidth = segmentWidth

    var leftX = 0
    var rightX = leftX + segmentWidth
    val topY = 0
    val botY = height

    for (i in 0..lastCompletedSegment - 1) {
      drawRoundedRect(canvas, leftX.toFloat(), topY.toFloat(), rightX.toFloat(), botY.toFloat(), fillRectanglePaint)
      leftX = leftX + segmentWidth + properties.segmentGapWidth
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

  private fun drawRoundedRect(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float, paint: Paint) {

    val path = Path()
    var rx = 6f
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
      rQuadTo(0f, -ry, -rx, -ry)//top-right corner
      rLineTo(-widthMinusCorners, 0f)
      rQuadTo(-rx, 0f, -rx, ry) //top-left corner
      rLineTo(0f, heightMinusCorners)

      rQuadTo(0f, ry, rx, ry)//bottom-left corner
      rLineTo(widthMinusCorners, 0f)
      rQuadTo(rx, 0f, rx, -ry) //bottom-right corner

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
