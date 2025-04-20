package com.example.tranquiltunes;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class NoiseGeneratorActivity extends AppCompatActivity implements SessionTimerDialog.TimerListener {

    private boolean isPlaying = false;
    private String noiseType = "White Noise";
    private float volumeLevel = 1.0f;
    private Handler handler = new Handler();
    private Runnable stopRunnable;
    private NoiseGenerator noiseGenerator;

    private ImageView playToggleBtn, timerBtn;
    private SeekBar volumeSlider;
    private TextView volumeTextView, timerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_generator);

        playToggleBtn = findViewById(R.id.playToggleBtn);
        volumeSlider = findViewById(R.id.volume_slider);
        volumeTextView = findViewById(R.id.tv_volume);
        timerBtn = findViewById(R.id.timerBtn);
        timerTextView = findViewById(R.id.timerTextView);

        noiseGenerator = new NoiseGenerator(noiseType);

        playToggleBtn.setOnClickListener(v -> toggleNoise());
        timerBtn.setOnClickListener(v -> showTimerDialog());

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeLevel = progress / 100.0f;
                volumeTextView.setText("Volume: " + progress + "%");
                noiseGenerator.setVolume(volumeLevel);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void toggleNoise() {
        if (isPlaying) {
            stopNoise();
            playToggleBtn.setImageResource(R.drawable.play100);
        } else {
            noiseGenerator.start();
            isPlaying = true;
            playToggleBtn.setImageResource(R.drawable.pause100);
        }
    }

    private void showTimerDialog() {
        DialogFragment timerDialog = new SessionTimerDialog();
        timerDialog.show(getSupportFragmentManager(), "session_timer");
    }

    @Override
    public void onTimeSelected(int minutes) {
        Log.d("Timer", "Session timer set for " + minutes + " minutes.");
        timerTextView.setText("Timer: " + minutes + " min");

        if (stopRunnable != null) {
            handler.removeCallbacks(stopRunnable); // Ensure no previous timer is running
        }

        stopRunnable = () -> {
            Log.d("Timer", "Stopping noise after " + minutes + " minutes.");
            stopNoise();
        };

        handler.postDelayed(stopRunnable, minutes * 60 * 1000); // Convert minutes to milliseconds
    }

    private void stopNoise() {
        isPlaying = false;
        noiseGenerator.stopNoise();
        playToggleBtn.setImageResource(R.drawable.play100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(stopRunnable);
        noiseGenerator.stopNoise();
    }
}
