package com.example.tranquiltunes;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicReference;

public class BinauralActivity extends AppCompatActivity {

    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private int sampleRate = 44100;
    private int bufferSize;
    private float volume = 0.5f;
    private int baseFrequency = 200;
    private int beatFrequency;

    private ImageView playButton;  // Reference to the ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural);

        TextView frequencyText = findViewById(R.id.frequencyText);
        SeekBar volumeControl = findViewById(R.id.volumeControl);
        playButton = findViewById(R.id.playButton);

        String frequencyLabel = getIntent().getStringExtra("frequency");
        frequencyText.setText("Playing: " + frequencyLabel);

        beatFrequency = extractFrequency(frequencyLabel);

        volumeControl.setProgress((int) (volume * 100));
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress / 100f;
                if (audioTrack != null) {
                    audioTrack.setVolume(volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopSound();
                playButton.setImageResource(R.drawable.play100);  // Change to play icon
            } else {
                playBinauralBeat();
                playButton.setImageResource(R.drawable.pause100);  // Change to stop icon
            }
        });
    }

    private int extractFrequency(String label) {
        return Integer.parseInt(label.replaceAll("[^0-9]", ""));
    }

    private void playBinauralBeat() {
        isPlaying = true;
        bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        short[] soundData = new short[bufferSize];
        final AtomicReference<Double>[] phaseLeft = new AtomicReference[]{new AtomicReference<>(0.0)};
        final double[] phaseRight = {0.0};
        double incrementLeft = 2.0 * Math.PI * baseFrequency / sampleRate;
        double incrementRight = 2.0 * Math.PI * (baseFrequency + beatFrequency) / sampleRate;

        new Thread(() -> {
            audioTrack.play();
            while (isPlaying) {
                for (int i = 0; i < bufferSize; i += 2) {
                    soundData[i] = (short) (Math.sin(phaseLeft[0].get()) * Short.MAX_VALUE * volume);
                    soundData[i + 1] = (short) (Math.sin(phaseRight[0]) * Short.MAX_VALUE * volume);
                    phaseLeft[0].updateAndGet(v -> new Double((double) (v + incrementLeft)));
                    phaseRight[0] += incrementRight;
                }
                audioTrack.write(soundData, 0, bufferSize);
            }
        }).start();
    }

    private void stopSound() {
        isPlaying = false;
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopSound();
    }
}
