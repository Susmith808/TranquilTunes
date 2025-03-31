package com.example.tranquiltunes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.*;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.LinkedList;
import java.util.Queue;

public class BreathingTrackerActivity extends AppCompatActivity {
    private static final String TAG = "BreathingTracker";
    private static final int AUDIO_SAMPLE_RATE = 44100;
    private static final int REQUEST_PERMISSION = 200;

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread audioThread;
    private MediaPlayer mediaPlayer;

    private TextView breathingTextView;
    private Queue<Double> amplitudeHistory = new LinkedList<>();
    private static final int HISTORY_SIZE = 20;
    private static final double FIXED_ENVIRONMENT_LOUDNESS = 7.0;
    private boolean isMusicPlaying = false;
    private float currentVolume = 0.1f;
    private float targetVolume = 0.1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_tracker);

        breathingTextView = findViewById(R.id.breathingTextView);
        mediaPlayer = MediaPlayer.create(this, R.raw.testpad);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(currentVolume, currentVolume);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_PERMISSION);
        } else {
            startAudioRecording();
        }
    }

    private void startAudioRecording() {
        int bufferSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_RECOGNITION, AUDIO_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);

        int audioSessionId = audioRecord.getAudioSessionId();
        if (NoiseSuppressor.isAvailable()) NoiseSuppressor.create(audioSessionId).setEnabled(true);
        if (AcousticEchoCanceler.isAvailable()) AcousticEchoCanceler.create(audioSessionId).setEnabled(true);
        if (AutomaticGainControl.isAvailable()) AutomaticGainControl.create(audioSessionId).setEnabled(true);

        isRecording = true;
        audioRecord.startRecording();

        audioThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            while (isRecording) {
                int readSize = audioRecord.read(buffer, 0, buffer.length);
                if (readSize > 0) {
                    double amplitude = processAudio(buffer, readSize);
                    updateBreathingStatus(amplitude);
                }
            }
        });
        audioThread.start();
    }

    private double processAudio(short[] buffer, int readSize) {
        double[] filteredSignal = applyBandPassFilter(buffer, readSize, 0.1, 3.0);
        return applyLowPassFilter(filteredSignal);
    }

    private double[] applyBandPassFilter(short[] buffer, int readSize, double lowCut, double highCut) {
        double[] filteredSignal = new double[readSize];
        double alphaLow = Math.exp(-2.0 * Math.PI * lowCut / AUDIO_SAMPLE_RATE);
        double alphaHigh = Math.exp(-2.0 * Math.PI * highCut / AUDIO_SAMPLE_RATE);
        double previousLow = 0, previousHigh = 0;

        for (int i = 0; i < readSize; i++) {
            double sample = buffer[i];
            double highPass = alphaHigh * (previousHigh + sample - (i > 0 ? buffer[i - 1] : 0));
            previousHigh = highPass;
            double bandPass = alphaLow * (previousLow + highPass - (i > 0 ? filteredSignal[i - 1] : 0));
            previousLow = bandPass;
            filteredSignal[i] = bandPass;
        }
        return filteredSignal;
    }

    private double applyLowPassFilter(double[] signal) {
        double alpha = 0.2;
        double smoothedAmplitude = 0;
        for (double value : signal) {
            smoothedAmplitude += alpha * (Math.abs(value) - smoothedAmplitude);
        }
        return Math.max(smoothedAmplitude, 0);
    }

    private void updateBreathingStatus(double amplitude) {
        amplitude = Math.abs(amplitude);
        if (amplitudeHistory.size() >= HISTORY_SIZE) amplitudeHistory.poll();
        amplitudeHistory.add(amplitude);

        String amplitudeText = String.format("Breathing Amplitude: %.3f | Fixed Baseline: %.3f", amplitude, FIXED_ENVIRONMENT_LOUDNESS);
        runOnUiThread(() -> breathingTextView.setText(amplitudeText));
        handleMusicPlayback(amplitude);
    }

    private void handleMusicPlayback(double amplitude) {
        double threshold = FIXED_ENVIRONMENT_LOUDNESS + 5;
        float maxVolume = 1.0f;
        float minVolume = 0.1f;

        if (amplitude > threshold) {
            if (!isMusicPlaying) {
                mediaPlayer.start();
                isMusicPlaying = true;
            }
            targetVolume = maxVolume;
        } else {
            targetVolume = minVolume;
        }
        adjustAudioVolume();
    }

    private void adjustAudioVolume() {
        new Thread(() -> {
            while (Math.abs(currentVolume - targetVolume) > 0.01) {
                currentVolume += (targetVolume - currentVolume) * 0.1;
                mediaPlayer.setVolume(currentVolume, currentVolume);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
