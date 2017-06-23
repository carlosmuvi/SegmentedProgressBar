package com.carlosmuvi.segmentedprogressbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet


data class PropertiesModel(val context: Context, val attrs: AttributeSet?) {

  var segmentCount: Int
  var containerColor: Int
  var fillColor: Int
  var segmentGapWidth: Int

  init {
    if (attrs != null) {
      val styledAttrs = context.theme.obtainStyledAttributes(attrs, R.styleable.SegmentedProgressBar, 0, 0)
      segmentCount = styledAttrs.getInt(R.styleable.SegmentedProgressBar_segment_count, 5)
      containerColor = styledAttrs.getColor(R.styleable.SegmentedProgressBar_container_color, Color.LTGRAY)
      fillColor = styledAttrs.getColor(R.styleable.SegmentedProgressBar_fill_color, Color.BLUE)
      segmentGapWidth = styledAttrs.getDimensionPixelSize(R.styleable.SegmentedProgressBar_gap_size, dpToPx(1))
    } else {
      segmentCount = 5
      containerColor = Color.LTGRAY
      fillColor = Color.BLUE
      segmentGapWidth = dpToPx(1)
    }
  }

  private fun dpToPx(valueInDp: Int): Int {
    val density = context.resources.displayMetrics.density
    return (valueInDp * density).toInt()
  }

}