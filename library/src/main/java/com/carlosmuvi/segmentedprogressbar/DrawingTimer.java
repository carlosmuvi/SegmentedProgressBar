package com.carlosmuvi.segmentedprogressbar;

import android.os.Handler;

public class DrawingTimer {

    private static final long TICK_TIME_MILLISECONDS = 10;

    private final Handler handler;
    private int totalTicks;
    private int currentTick = 0;
    private Listener listener;
    private TimerState timerState = TimerState.IDLE;

    public DrawingTimer() {
        handler = new Handler();
    }

    public void start(long timeInMilliseconds) {
        requireMinimumTickTime(timeInMilliseconds);
        if (timerState == TimerState.IDLE) {
            this.totalTicks = (int) (timeInMilliseconds / TICK_TIME_MILLISECONDS);
        }
        if (timerState != TimerState.RUNNING) {
            timerState = TimerState.RUNNING;
            runDrawingTask();
        }
    }

    private void requireMinimumTickTime(long timeInMilliseconds) {
        if (timeInMilliseconds < TICK_TIME_MILLISECONDS) {
            String errorMessage = "A minimum of " +
                    TICK_TIME_MILLISECONDS +
                    " milliseconds is required, but input is " +
                    timeInMilliseconds + " milliseconds.";
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void runDrawingTask() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onTick(currentTick, totalTicks);
                currentTick++;
                if (currentTick <= totalTicks) {
                    handler.postDelayed(this, TICK_TIME_MILLISECONDS);
                } else {
                    reset();
                }
            }
        });
    }

    public void pause() {
        if (timerState == TimerState.RUNNING) {
            timerState = TimerState.PAUSED;
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void resume() {
        if (timerState == TimerState.PAUSED) {
            timerState = TimerState.RUNNING;
            runDrawingTask();
        }
    }

    public void reset() {
        pause();
        timerState = TimerState.IDLE;
        currentTick = 0;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean isRunning() {
        return timerState == TimerState.RUNNING;
    }

    public boolean isPaused() {
        return timerState == TimerState.PAUSED;
    }

    enum TimerState {
        RUNNING, PAUSED, IDLE
    }

    interface Listener {
        void onTick(int currentTicks, int totalTicks);
    }
}
