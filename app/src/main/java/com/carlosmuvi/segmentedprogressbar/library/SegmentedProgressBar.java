package com.carlosmuvi.segmentedprogressbar.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by carlosmuvi on 02/09/16.
 */

public class SegmentedProgressBar extends View {

    private static final int SEGMENT_COUNT = 5;
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
                int segmentWidth = getWidth() / SEGMENT_COUNT;

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
        int segmentWidth = getWidth() / SEGMENT_COUNT;

        int leftX = 0;
        int rightX = leftX + segmentWidth;
        int topY = 0;
        int botY = getHeight();

        for (int i = 0; i < SEGMENT_COUNT; i++) {
            canvas.drawRect(leftX, topY, rightX, botY, containerRectanglePaint);
            leftX = leftX + segmentWidth;
            rightX = rightX + segmentWidth;
        }
    }

    private void drawCompletedRectangles(Canvas canvas) {
        int segmentWidth = getWidth() / SEGMENT_COUNT;

        int leftX = 0;
        int rightX = leftX + segmentWidth;
        int topY = 0;
        int botY = getHeight();

        for (int i = 0; i < lastCompletedSegment; i++) {
            canvas.drawRect(leftX, topY, rightX, botY, fillRectanglePaint);
            leftX = leftX + segmentWidth;
            rightX = rightX + segmentWidth;
        }
    }

    private void drawCurrentRectangle(Canvas canvas) {
        int segmentWidth = getWidth() / SEGMENT_COUNT;

        int leftX = lastCompletedSegment * segmentWidth;
        int rightX = leftX + currentSegmentProgressInPx;
        int topY = 0;
        int botY = getHeight();
        canvas.drawRect(leftX, topY, rightX, botY, fillRectanglePaint);
    }

    private Paint buildFillRectanglePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private Paint buildContainerRectanglePaint() {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }
}
