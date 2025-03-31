package com.example.tranquiltunes;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class NoiseGeneratorActivity extends AppCompatActivity {

    private static final int SAMPLE_RATE = 44100;
    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private Thread noiseThread;
    private String noiseType = "whitenoise";
    private float volumeLevel = 1.0f;

    private ImageView playToggleBtn;
    private SeekBar volumeSlider;
    private TextView volumeTextView, stateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noise_generator);

        playToggleBtn = findViewById(R.id.playToggleBtn);
        volumeSlider = findViewById(R.id.volume_slider);
        volumeTextView = findViewById(R.id.tv_volume);
        stateTextView = findViewById(R.id.stateText);


        playToggleBtn.setOnClickListener(v -> toggleNoise());
        

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volumeLevel = progress / 100.0f;
                volumeTextView.setText("Volume: " + progress + "%");
                if (audioTrack != null) {
                    audioTrack.setStereoVolume(volumeLevel, volumeLevel);
                }
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
            playToggleBtn.setImageResource(R.drawable.play);
        } else {
            playNoise();
            playToggleBtn.setImageResource(R.drawable.pause);
        }
    }

    private void showSoundPicker() {
        String[] soundOptions = {"White Noise", "Pink Noise", "Brown Noise"};
        new AlertDialog.Builder(this)
                .setTitle("Select Noise Type")
                .setItems(soundOptions, (dialog, which) -> {
                    noiseType = soundOptions[which].toLowerCase().replace(" ", "");
                    stateTextView.setText("State: " + soundOptions[which]);
                })
                .show();
    }

    private void playNoise() {
        stopNoise();
        int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);
        audioTrack.setStereoVolume(volumeLevel, volumeLevel);

        noiseThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            Random random = new Random();
            isPlaying = true;
            audioTrack.play();
            while (isPlaying) {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (short) (generateNoiseSample(random) * Short.MAX_VALUE);
                }
                audioTrack.write(buffer, 0, buffer.length);
            }
        });
        noiseThread.start();
    }

    private double generateNoiseSample(Random random) {
        switch (noiseType) {
            case "pinknoise": return (random.nextGaussian() * 0.5);
            case "brownnoise": return (random.nextDouble() * 0.2 - 0.1);
            default: return (random.nextDouble() * 2 - 1); // White Noise
        }
    }

    private void stopNoise() {
        isPlaying = false;
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
        if (noiseThread != null) {
            noiseThread.interrupt();
            noiseThread = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopNoise();
    }
}
