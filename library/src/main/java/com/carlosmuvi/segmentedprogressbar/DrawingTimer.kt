package com.carlosmuvi.segmentedprogressbar

import android.os.Handler

/**
 * Created by carlosmuvi on 02/09/16.
 */

class DrawingTimer(var listener: ((Int, Int) -> Unit)?) {

  private val handler: Handler = Handler()
  private val tickTimeInMilliseconds: Long = 30
  private var totalTicks: Int = 0
  private var currentTick = 0
  private var timerState = TimerState.IDLE

  fun start(timeInMilliseconds: Long) {
    if (timerState == TimerState.IDLE) {
      this.totalTicks = (timeInMilliseconds / tickTimeInMilliseconds).toInt()
    }
    if (timerState != TimerState.RUNNING) {
      timerState = TimerState.RUNNING
      runDrawingTask()
    }
  }

  private fun runDrawingTask() {
    handler.post(object : Runnable {
      override fun run() {
        listener?.invoke(currentTick, totalTicks)
        currentTick++
        if (currentTick <= totalTicks) {
          handler.postDelayed(this, tickTimeInMilliseconds)
        } else {
          reset()
        }
      }
    })
  }

  fun pause() {
    if (timerState == TimerState.RUNNING) {
      timerState = TimerState.PAUSED
      handler.removeCallbacksAndMessages(null)
    }
  }

  fun resume() {
    if (timerState == TimerState.PAUSED) {
      timerState = TimerState.RUNNING
      runDrawingTask()
    }
  }

  fun reset() {
    pause()
    timerState = TimerState.IDLE
    currentTick = 0
  }

  val isRunning: Boolean
    get() = timerState == TimerState.RUNNING

  val isPaused: Boolean
    get() = timerState == TimerState.PAUSED

  internal enum class TimerState {
    RUNNING, PAUSED, IDLE
  }
}
