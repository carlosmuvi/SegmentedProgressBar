package com.carlosmuvi.segmentedprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.carlosmuvi.segmentedprogressbar.R;
import com.carlosmuvi.segmentedprogressbar.library.SegmentedProgressBar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SegmentedProgressBar segmentedProgressBar;
    Button startButton;
    Button pauseButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        startButton = (Button) findViewById(R.id.button);
        pauseButton = (Button) findViewById(R.id.button3);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Random rand = new Random();
                long randomNum = 3000 + rand.nextInt((7000) + 1);
                segmentedProgressBar.playSegment(randomNum);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                segmentedProgressBar.pause();
            }
        });
    }
}
