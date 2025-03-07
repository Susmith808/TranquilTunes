package com.example.tranquiltunes;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class BinauralBeatsActivity extends AppCompatActivity {

    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private Thread audioThread;

    private int baseFrequency = 200;  // Default base frequency
    private int binauralFrequency = 8; // Default binaural beat difference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural_beats);

        RadioGroup frequencyGroup = findViewById(R.id.frequencyGroup);
        Button toggleButton = findViewById(R.id.toggleButton);

        // Handle frequency selection
        frequencyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.relaxation) {
                binauralFrequency = 8;
                baseFrequency = 200;
            } else if (checkedId == R.id.sleep) {
                binauralFrequency = 4;
                baseFrequency = 180;
            } else if (checkedId == R.id.focus) {
                binauralFrequency = 14;
                baseFrequency = 220;
            } else {
                binauralFrequency = 8;
                baseFrequency = 200; // Default case
            }
        });

        // Toggle binaural beats playback
        toggleButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopBinauralBeats();
                toggleButton.setText("Start");
            } else {
                startBinauralBeats();
                toggleButton.setText("Stop");
            }
        });
    }

    private void startBinauralBeats() {
        isPlaying = true;
        int sampleRate = 44100;
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);

        audioThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            double phaseLeft = 0, phaseRight = 0;
            double incrementLeft = 2 * Math.PI * baseFrequency / sampleRate;
            double incrementRight = 2 * Math.PI * (baseFrequency + binauralFrequency) / sampleRate;

            audioTrack.play();

            while (isPlaying) {
                for (int i = 0; i < bufferSize / 2; i++) {  // Stereo: left & right samples
                    buffer[2 * i] = (short) (Math.sin(phaseLeft) * Short.MAX_VALUE); // Left channel
                    buffer[2 * i + 1] = (short) (Math.sin(phaseRight) * Short.MAX_VALUE); // Right channel

                    phaseLeft += incrementLeft;
                    phaseRight += incrementRight;

                    if (phaseLeft > 2 * Math.PI) phaseLeft -= 2 * Math.PI;
                    if (phaseRight > 2 * Math.PI) phaseRight -= 2 * Math.PI;
                }
                audioTrack.write(buffer, 0, buffer.length);
            }
        });

        audioThread.start();
    }

    private void stopBinauralBeats() {
        isPlaying = false;

        if (audioThread != null) {
            try {
                audioThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioThread = null;
        }

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBinauralBeats();
    }
}
