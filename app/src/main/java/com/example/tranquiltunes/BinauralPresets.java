package com.example.tranquiltunes;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class BinauralPresets extends AppCompatActivity {

    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private static final int SAMPLE_RATE = 44100;
    private int beatFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural_presets);

        // Get data from intent
        String beatName = getIntent().getStringExtra("BEAT_NAME");
        beatFrequency = getIntent().getIntExtra("BEAT_FREQUENCY", 10);

        // Set the title
        TextView title = findViewById(R.id.beatTitle);
        title.setText(beatName);

        Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                stopBinauralBeat();
                playButton.setText("Play");
                playButton.setBackgroundColor(Color.GREEN);
            } else {
                startBinauralBeat();
                playButton.setText("Stop");
                playButton.setBackgroundColor(Color.RED);
            }
        });
    }

    private void startBinauralBeat() {
        isPlaying = true;
        new Thread(() -> {
            int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

            short[] buffer = new short[bufferSize];
            double baseFrequency = 200;
            double leftFreq = baseFrequency - (beatFrequency / 2.0);
            double rightFreq = baseFrequency + (beatFrequency / 2.0);

            audioTrack.play();

            for (int i = 0; isPlaying; i++) {
                double sampleLeft = Math.sin(2 * Math.PI * leftFreq * i / SAMPLE_RATE);
                double sampleRight = Math.sin(2 * Math.PI * rightFreq * i / SAMPLE_RATE);
                buffer[i % bufferSize] = (short) ((sampleLeft + sampleRight) / 2 * Short.MAX_VALUE);

                if (i % bufferSize == bufferSize - 1) {
                    audioTrack.write(buffer, 0, bufferSize);
                }
            }
        }).start();
    }

    private void stopBinauralBeat() {
        if (audioTrack != null) {
            isPlaying = false;
            audioTrack.stop();
            audioTrack.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBinauralBeat();
    }
}
