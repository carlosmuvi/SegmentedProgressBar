package com.carlosmuvi.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

public class MainActivity extends AppCompatActivity {

    SegmentedProgressBar segmentedProgressBar;
    Button startButton;
    Button pauseButton;
    Button resetButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSegmentedProgressBar();

        startButton = (Button) findViewById(R.id.button);
        pauseButton = (Button) findViewById(R.id.button3);
        resetButton = (Button) findViewById(R.id.button2);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                segmentedProgressBar.playSegment(3000);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                segmentedProgressBar.pause();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                segmentedProgressBar.reset();
            }
        });
    }

    private void initSegmentedProgressBar() {
        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);

        //set filled segments directly
        segmentedProgressBar.setCompletedSegments(1);
    }
}
