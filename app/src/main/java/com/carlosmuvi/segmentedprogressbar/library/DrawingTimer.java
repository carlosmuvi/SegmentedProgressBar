package com.carlosmuvi.segmentedprogressbar.library;

import android.os.Handler;

/**
 * Created by carlosmuvi on 02/09/16.
 */

public class DrawingTimer {

    private final Handler handler;
    private final long tickTimeInMilliseconds;
    private final int totalTicks;
    private int currentTick = 0;
    private Listener listener;

    public DrawingTimer(long timeInMilliseconds, long tickTimeInMilliseconds) {
        this.tickTimeInMilliseconds = tickTimeInMilliseconds;
        this.totalTicks = (int) (timeInMilliseconds / tickTimeInMilliseconds);
        handler = new Handler();
    }

    public void start() {
        handler.post(new Runnable() {
            @Override public void run() {
                listener.onTick(currentTick, totalTicks);
                currentTick++;
                if (currentTick <= totalTicks) {
                    handler.postDelayed(this, tickTimeInMilliseconds);
                } else {
                    reset();
                }
            }
        });
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }

    public void reset() {
        stop();
        currentTick = 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onTick(int currentTicks, int totalTicks);
    }
}
