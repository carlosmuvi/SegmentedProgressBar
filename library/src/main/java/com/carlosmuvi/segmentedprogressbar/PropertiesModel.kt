package com.carlosmuvi.segmentedprogressbar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import java.math.BigDecimal

class PropertiesModel(context: Context, attrs: AttributeSet?)
{

    var segmentCount: Int = DEFAULT_SEGMENT_COUNT
    var containerColor: Int = Color.LTGRAY
    var fillColor: Int = Color.BLUE
    var segmentGapWidth: Int = context.dp(DEFAULT_SEGMENT_GAP_DP)
    var cornerRadius: Int = context.dp(DEFAULT_CORNER_RADIUS_DP)
    var progressValuePerContainer: Int = 50;
    var progressFilledContainers: BigDecimal = BigDecimal("0.0")
    var progressValue: Float = 0f
    var segmentedContainerColors: HashMap<Int, Int> = HashMap<Int, Int>()
    init
    {


        if (attrs != null)
        {


            val styledAttrs =
                    context.theme.obtainStyledAttributes(attrs, R.styleable.SegmentedProgressBar, 0, 0)
            segmentCount =
                    styledAttrs.getInt(R.styleable.SegmentedProgressBar_segment_count, 5)
            containerColor =
                    styledAttrs.getColor(R.styleable.SegmentedProgressBar_container_color, containerColor)
            fillColor =
                    styledAttrs.getColor(R.styleable.SegmentedProgressBar_fill_color, fillColor)
            segmentGapWidth =
                    styledAttrs.getDimensionPixelSize(R.styleable.SegmentedProgressBar_gap_size, segmentGapWidth)
            cornerRadius =
                    styledAttrs.getDimensionPixelSize(R.styleable.SegmentedProgressBar_corner_radius, cornerRadius)
            val totalProgress: Int = styledAttrs.getInt(R.styleable.SegmentedProgressBar_total_progress, 100)

            Log.d("hefere", "progressValue : " + progressValue)
            Log.d("hefere", "segmentCount : " + segmentCount)

            progressValue = styledAttrs.getFloat(R.styleable.SegmentedProgressBar_progress, 0f)

            calculateContainerValue(totalProgress)


        }

    }

    fun calculateContainerValue(totalProgress: Int = 100)
    {

        progressValuePerContainer = totalProgress / segmentCount;

        if (progressValue != 0f)
            progressFilledContainers = BigDecimal((progressValue / progressValuePerContainer).toString())
    }

    companion object
    {
        const val DEFAULT_SEGMENT_COUNT = 5
        const val DEFAULT_CORNER_RADIUS_DP = 6
        const val DEFAULT_SEGMENT_GAP_DP = 1
    }
}
