package com.example.tranquiltunes;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class Breathdetector extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float baselineY = 0;
    private boolean isBaselineSet = false;
    private int baselineReadingsCount = 0;
    private static final int BASELINE_SAMPLES = 50; // Number of readings to determine baseline

    private static final float THRESHOLD = 0.02f;  // Adjusted sensitivity
    private static final float ALPHA = 0.8f;  // Exponential moving average smoothing
    private static final float MAX_MOVEMENT = 0.8f; // Define max movement range for volume scaling

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private Handler handler = new Handler();

    private int currentVolume = 1;
    private boolean isPlaying = false;

    private TextView sensorDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathdetector);

        sensorDataTextView = findViewById(R.id.sensorDataText);

        // Initialize MediaPlayer
        mediaPlayer = MediaPlayer.create(this, R.raw.pink);
        mediaPlayer.setLooping(true);

        // Initialize AudioManager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Request audio focus
        audioManager.requestAudioFocus(focusChange -> {
            if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                if (!mediaPlayer.isPlaying() && isPlaying) {
                    mediaPlayer.start();
                }
            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // Initialize SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        if (accelerometer == null) {
            Log.e("BreathDetector", "No Accelerometer Found!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.pink);
            mediaPlayer.setLooping(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (sensorManager != null && accelerometer != null) {
            sensorManager.unregisterListener(this);
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float rawY = event.values[1];
            float y = applyLowPassFilter(rawY, baselineY);

            if (!isBaselineSet) {
                baselineY += y;
                baselineReadingsCount++;

                if (baselineReadingsCount >= BASELINE_SAMPLES) {
                    baselineY /= BASELINE_SAMPLES; // Average out the baseline
                    isBaselineSet = true;
                    Log.d("BreathDetector", "Baseline Y set: " + baselineY);
                }
                return;
            }

            float movement = y - baselineY;
            float normalizedMovement = Math.max(0, Math.min(movement / MAX_MOVEMENT, 1));

            sensorDataTextView.setText(String.format("Y-Axis: %.3f\nBaseline: %.3f\nMovement: %.3f", y, baselineY, movement));
            Log.d("BreathDetector", "Current Y: " + y + ", Baseline: " + baselineY + ", Normalized: " + normalizedMovement);

            adjustVolume(normalizedMovement);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void adjustVolume(float intensity) {
        final int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final int targetVolume = (int) (intensity * maxVolume);

        if (!isPlaying && targetVolume > 1) {
            mediaPlayer.start();
            isPlaying = true;
        }

        if (targetVolume == 0) {
            mediaPlayer.pause();
            isPlaying = false;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentVolume < targetVolume) {
                    currentVolume++;
                } else if (currentVolume > targetVolume) {
                    currentVolume--;
                }

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);

                if (currentVolume != targetVolume) {
                    handler.postDelayed(this, 300);
                }
            }
        }, 300);
    }

    private float applyLowPassFilter(float newValue, float oldValue) {
        return ALPHA * oldValue + (1 - ALPHA) * newValue;
    }
}
