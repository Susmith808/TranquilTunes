package com.example.tranquiltunes;

import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BinauralPresets extends AppCompatActivity {

    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private static final int SAMPLE_RATE = 44100;
    private int beatFrequency;
    private Thread audioThread;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural_presets);

        // Get data from intent safely
        String beatName = getIntent().getStringExtra("BEAT_NAME");
        beatFrequency = getIntent().getIntExtra("BEAT_FREQUENCY", -1);

        if (beatName == null || beatFrequency == -1) {
            Toast.makeText(this, "Error loading binaural beat", Toast.LENGTH_SHORT).show();
            finish(); // Close activity to prevent crash
            return;
        }

        // Set the title
        TextView title = findViewById(R.id.beatTitle);
        title.setText(beatName);

        TextView frequencyText = findViewById(R.id.beatFrequency);
        frequencyText.setText("Frequency: " + beatFrequency + " Hz");

        playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                stopBinauralBeat();
            } else {
                startBinauralBeat();
            }
        });
    }

    private void startBinauralBeat() {
        isPlaying = true;
        runOnUiThread(() -> {
            playButton.setText("Stop");
            playButton.setBackgroundColor(Color.RED);
        });

        audioThread = new Thread(() -> {
            try {
                int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);

                if (bufferSize == AudioTrack.ERROR || bufferSize == AudioTrack.ERROR_BAD_VALUE) {
                    return;
                }

                audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                        AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize, AudioTrack.MODE_STREAM);

                short[] buffer = new short[bufferSize / 2];
                double leftFreq = 200 - (beatFrequency / 2.0);
                double rightFreq = 200 + (beatFrequency / 2.0);
                double phaseLeft = 0, phaseRight = 0;
                double incrementLeft = 2 * Math.PI * leftFreq / SAMPLE_RATE;
                double incrementRight = 2 * Math.PI * rightFreq / SAMPLE_RATE;

                audioTrack.play();

                while (isPlaying) {
                    for (int i = 0; i < buffer.length; i += 2) {
                        phaseLeft += incrementLeft;
                        phaseRight += incrementRight;

                        buffer[i] = (short) (Math.sin(phaseLeft) * Short.MAX_VALUE);
                        buffer[i + 1] = (short) (Math.sin(phaseRight) * Short.MAX_VALUE);
                    }
                    audioTrack.write(buffer, 0, buffer.length);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        audioThread.start();
    }

    private void stopBinauralBeat() {
        isPlaying = false;
        runOnUiThread(() -> {
            playButton.setText("Play");
            playButton.setBackgroundColor(Color.GREEN);
        });

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopBinauralBeat();
    }
}
