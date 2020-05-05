package com.carlosmuvi.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carlosmuvi.segmentedprogressbar.CompletedSegmentListener
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar

class MainActivity : AppCompatActivity() {
    lateinit var segmentedProgressBar: SegmentedProgressBar
    lateinit var startButton: Button
    lateinit var startWithoutAnimationButton: Button
    lateinit var pauseResumeButton: Button
    lateinit var resetButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSegmentedProgressBar()
        startButton = findViewById(R.id.button)
        pauseResumeButton = findViewById(R.id.button3)
        resetButton = findViewById(R.id.button2)
        startWithoutAnimationButton = findViewById(R.id.button4)
        startButton.setOnClickListener(View.OnClickListener {
            //                segmentedProgressBar.playSegment(3000);
            var percentage = 0F

//            val t = Thread() {
//                val oldProgress = 0
//                while (percentage <= 100F) {
//                    this.runOnUiThread {
//                        percentage += (10F / 100F)
//                        Log.d("oskatest2", "showing percentage $percentage")
//                        segmentedProgressBar!!.updateProgress(percentage)
//                    }
//                    Thread.sleep(10)
//                }
//            }
//            t.run()

//            while (percentage <= 100F) {
//                percentage += (10F / 100F)
//                Log.d("oskatest2" , "showing percentage $percentage")
//                segmentedProgressBar!!.updateProgress(percentage)
//                try {
//                    Thread.sleep(1000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
            while (percentage <= 100F) {
                percentage += (10F / 100F)
                Log.d("oskatest2" , "showing percentage $percentage")
                segmentedProgressBar!!.updateProgress(percentage)
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        })
        pauseResumeButton.setOnClickListener(View.OnClickListener {
            if (segmentedProgressBar!!.isPaused()!!) {
                segmentedProgressBar!!.resume()
            } else {
                segmentedProgressBar!!.pause()
            }
        })
        startWithoutAnimationButton.setOnClickListener(View.OnClickListener {
            var percentage = 0F

            val t = Thread() {
                while (percentage <= 100F) {
                    this.runOnUiThread {
                        percentage += (10F / 100F)
                        Log.d("oskatest2", "showing percentage $percentage")
                        segmentedProgressBar!!.updateProgress(percentage)
                    }
                    Thread.sleep(10)
                }
            }


//            while (percentage <= 100) {
//                percentage += 10
//                segmentedProgressBar!!.updateProgress(percentage.toFloat())
//                try {
//                    Thread.sleep(100)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//            }
        })
        resetButton.setOnClickListener(View.OnClickListener { segmentedProgressBar!!.reset() })
    }

    private fun initSegmentedProgressBar() {
        segmentedProgressBar = findViewById<SegmentedProgressBar>(R.id.segmented_progressbar)
        //set filled segments directly
        segmentedProgressBar.setCompletedSegments(1)
        segmentedProgressBar.setCompletedSegmentListener(object : CompletedSegmentListener {
            override fun onSegmentCompleted(segmentCount: Int) {
                Toast.makeText(
                    this@MainActivity,
                    "Completed segment$segmentCount",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}