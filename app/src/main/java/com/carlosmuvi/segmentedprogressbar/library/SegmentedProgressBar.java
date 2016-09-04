package com.carlosmuvi.segmentedprogressbar.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by carlosmuvi on 02/09/16.
 */

public class SegmentedProgressBar extends View {

    private static final int SEGMENT_COUNT = 2;
    private static final long SEGMENT_FILL_TIME_MILLISECONDS = 3000;

    private int lastCompletedSegment = 0;
    private int currentSegmentProgressInPx = 0;

    private Paint containerRectanglePaint;
    private Paint fillRectanglePaint;
    private DrawingTimer drawingTimer;

    public SegmentedProgressBar(Context context) {
        super(context);
        initView();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        containerRectanglePaint = buildContainerRectanglePaint();
        fillRectanglePaint = buildFillRectanglePaint();
        drawingTimer = new DrawingTimer(SEGMENT_FILL_TIME_MILLISECONDS, 30L);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawContainerRectangles(canvas);
        drawCompletedRectangles(canvas);
        drawCurrentRectangle(canvas);
    }

    /*
    EXPOSED METHODS
     */

    public void playSegment() {
        drawingTimer.setListener(new DrawingTimer.Listener() {
            @Override public void onTick(int currentTicks, int totalTicks) {

                int segmentWidth = getSegmentWidth();

                currentSegmentProgressInPx = currentTicks * segmentWidth / totalTicks;
                if (totalTicks <= currentTicks) {
                    lastCompletedSegment++;
                    currentSegmentProgressInPx = 0;
                }
                invalidate();
            }
        });
        drawingTimer.start();
    }

    /*
    PRIVATE METHODS
     */

    private void drawContainerRectangles(Canvas canvas) {
        int segmentWidth = getSegmentWidth();

        int leftX = 0;
        int rightX = leftX + segmentWidth;
        int topY = 0;
        int botY = getHeight();

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            drawRoundedRect(canvas, leftX, topY, rightX, botY, containerRectanglePaint);
            leftX = leftX + segmentWidth + getSegmentGapWidth();
            rightX = leftX + segmentWidth;
        }
    }

    private void drawCompletedRectangles(Canvas canvas) {
        int segmentWidth = getSegmentWidth();

        int leftX = 0;
        int rightX = leftX + segmentWidth;
        int topY = 0;
        int botY = getHeight();

        for (int i = 0; i < lastCompletedSegment; i++) {
            drawRoundedRect(canvas, leftX, topY, rightX, botY, fillRectanglePaint);
            leftX = leftX + segmentWidth + getSegmentGapWidth();
            rightX = leftX + segmentWidth;
        }
    }

    private void drawCurrentRectangle(Canvas canvas) {
        int segmentWidth = getSegmentWidth();

        int leftX = lastCompletedSegment * (segmentWidth + getSegmentGapWidth());
        int rightX = leftX + currentSegmentProgressInPx;
        int topY = 0;
        int botY = getHeight();
        drawRoundedRect(canvas, leftX, topY, rightX, botY, fillRectanglePaint);
    }

    private void drawRoundedRect(Canvas canvas, float left, float top, float right, float bottom, Paint paint) {

        Path path = new Path();
        float rx = 6;
        if (rx < 0) rx = 0;
        float ry = 6;
        if (ry < 0) ry = 0;
        float width = right - left;
        float height = bottom - top;
        if (rx > width / 2) rx = width / 2;
        if (ry > height / 2) ry = height / 2;
        float widthMinusCorners = (width - (2 * rx));
        float heightMinusCorners = (height - (2 * ry));

        path.moveTo(right, top + ry);
        path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
        path.rLineTo(-widthMinusCorners, 0);
        path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
        path.rLineTo(0, heightMinusCorners);

        path.rQuadTo(0, ry, rx, ry);//bottom-left corner
        path.rLineTo(widthMinusCorners, 0);
        path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner

        path.rLineTo(0, -heightMinusCorners);

        path.close();//Given close, last lineto can be removed.

        canvas.drawPath(path, paint);
    }

    private Paint buildFillRectanglePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private Paint buildContainerRectanglePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private int getSegmentWidth() {
        return (getWidth() / SEGMENT_COUNT) - getSegmentGapWidth();
    }

    private int getSegmentGapWidth() {
        return dpToPx(1);
    }

    private int dpToPx(int valueInDp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (valueInDp * density);
    }
}
