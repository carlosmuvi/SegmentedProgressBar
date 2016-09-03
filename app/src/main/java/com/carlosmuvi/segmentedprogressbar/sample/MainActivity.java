package com.carlosmuvi.segmentedprogressbar.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.carlosmuvi.segmentedprogressbar.R;
import com.carlosmuvi.segmentedprogressbar.library.SegmentedProgressBar;

public class MainActivity extends AppCompatActivity {

    SegmentedProgressBar segmentedProgressBar;
    Button startButton;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        segmentedProgressBar = (SegmentedProgressBar) findViewById(R.id.segmented_progressbar);
        startButton = (Button) findViewById(R.id.button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                segmentedProgressBar.playSegment();
            }
        });
    }
}
