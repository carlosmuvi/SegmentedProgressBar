package com.carlosmuvi.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;

public class MainActivity extends AppCompatActivity {

    SegmentedProgressBar segmentedProgressBar;
    Button startButton;
    Button pauseButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSegmentedProgressBar();

        startButton = (Button) findViewById(R.id.button);
        pauseButton = (Button) findViewById(R.id.button3);

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
    }

    private void initSegmentedProgressBar() {
        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        segmentedProgressBar.setSegmentCount(7); // number of segments in your bar

        //customize colors.
        segmentedProgressBar.setContainerColor(Color.BLUE); //empty segment color
        segmentedProgressBar.setFillColor(Color.GREEN); //empty segment color

        //set filled segments directly
        segmentedProgressBar.setCompletedSegments(3);
    }
}
