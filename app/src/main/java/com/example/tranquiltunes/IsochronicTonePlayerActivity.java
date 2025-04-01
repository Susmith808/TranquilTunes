package com.example.tranquiltunes;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class IsochronicTonePlayerActivity extends AppCompatActivity {

    private IsochronicToneGenerator toneGenerator;
    private double toneFrequency;
    private double pulseRate = 10.0;
    private double volume = 0.5;
    private int sessionDuration = 5; // Default in minutes

    // UI Elements
    private TextView toneTitle, pulseTextView, volumeTextView, sessionTextView;
    private ImageView playToggleButton, setSessionButton;
    private SeekBar pulseRateSeekBar, volumeSeekBar;

    private Handler handler = new Handler();
    private Runnable stopRunnable;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isochronic_tone_player);

        // Initialize UI Elements
        toneTitle = findViewById(R.id.toneTitle);
        pulseTextView = findViewById(R.id.pulseTextView);
        volumeTextView = findViewById(R.id.volumeTextView);
        sessionTextView = findViewById(R.id.sessionTextView);
        playToggleButton = findViewById(R.id.playToggleButton);
        setSessionButton = findViewById(R.id.setSessionButton);
        pulseRateSeekBar = findViewById(R.id.pulseRateSeekBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);

        toneGenerator = new IsochronicToneGenerator();

        // Retrieve data from intent
        toneFrequency = getIntent().getDoubleExtra("toneFrequency", 300.0);
        pulseRate = getIntent().getDoubleExtra("pulseRate", 10.0);
        String toneName = getIntent().getStringExtra("toneName");
        toneTitle.setText(toneName != null ? toneName : "Isochronic Tone");

        // Update UI
        pulseTextView.setText("Pulse Rate: " + pulseRate + " Hz");
        volumeTextView.setText("Volume: " + volume);
        updateSessionText();

        // Toggle Play/Stop
        playToggleButton.setOnClickListener(v -> togglePlayback());

        // Session Time Picker
        setSessionButton.setOnClickListener(v -> showSessionPickerDialog());

        // Pulse Rate SeekBar Listener
        pulseRateSeekBar.setMax(30);
        pulseRateSeekBar.setProgress((int) pulseRate);
        pulseRateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pulseRate = progress;
                pulseTextView.setText("Pulse Rate: " + pulseRate + " Hz");

                if (isPlaying) {
                    toneGenerator.updatePulseRate(pulseRate);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Volume Control SeekBar
        volumeSeekBar.setMax(100);
        volumeSeekBar.setProgress((int) (volume * 100));
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress / 100.0;
                volumeTextView.setText("Volume: " + volume);

                if (isPlaying) {
                    toneGenerator.updateVolume(volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Toggle Play and Stop
    private void togglePlayback() {
        if (isPlaying) {
            stopTone();
        } else {
            startTone();
        }
    }

    // Start Tone Generation
    private void startTone() {
        isPlaying = true;
        toneGenerator.playIsochronicTone(toneFrequency, pulseRate, volume);
        playToggleButton.setImageResource(R.drawable.pause100); // Change icon to "Pause"

        stopRunnable = this::stopTone;
        handler.postDelayed(stopRunnable, sessionDuration * 60 * 1000);
    }

    // Stop Tone Generation
    private void stopTone() {
        isPlaying = false;
        toneGenerator.stopIsochronicTone();
        playToggleButton.setImageResource(R.drawable.play100); // Change icon back to "Play"
        handler.removeCallbacks(stopRunnable);
    }

    // Session Picker Dialog
    private void showSessionPickerDialog() {
        Dialog dialog = new Dialog(IsochronicTonePlayerActivity.this);
        dialog.setContentView(R.layout.dialog_number_picker);

        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);
        ImageView confirmButton = dialog.findViewById(R.id.confirmButton);

        if (numberPicker == null || confirmButton == null) {
            dialog.dismiss();
            return;
        }

        int[] sessionOptions = {1, 2, 3, 4, 5, 10, 15, 20, 30, 45, 60};
        String[] sessionLabels = new String[sessionOptions.length];
        for (int i = 0; i < sessionOptions.length; i++) {
            sessionLabels[i] = sessionOptions[i] + " min";
        }

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(sessionOptions.length - 1);
        numberPicker.setDisplayedValues(sessionLabels);
        numberPicker.setValue(4); // Default to 5 minutes

        confirmButton.setOnClickListener(v -> {
            sessionDuration = sessionOptions[numberPicker.getValue()];
            updateSessionText();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Update Session Text
    private void updateSessionText() {
        sessionTextView.setText("Session Duration: " + sessionDuration + " min");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTone();
    }
}
