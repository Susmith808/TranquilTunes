package com.example.tranquiltunes;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.Random;

public class NoiseGenerator {
    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private Thread noiseThread;
    private String noiseType;

    public NoiseGenerator(String noiseType) {
        this.noiseType = noiseType;
    }

    public void start() {
        if (isPlaying) return;

        isPlaying = true;
        int sampleRate = 44100;
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);

        noiseThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            Random random = new Random();

            audioTrack.play();

            while (isPlaying) {
                for (int i = 0; i < bufferSize; i++) {
                    buffer[i] = generateNoiseSample(noiseType, random);
                }
                audioTrack.write(buffer, 0, buffer.length);
            }
        });

        noiseThread.start();
    }

    private short generateNoiseSample(String noiseType, Random random) {
        switch (noiseType) {
            case "White Noise":
                return (short) (random.nextInt(Short.MAX_VALUE * 2) - Short.MAX_VALUE);
            case "Pink Noise":
                return (short) (random.nextGaussian() * Short.MAX_VALUE * 0.5);
            case "Brown Noise":
                return (short) (random.nextGaussian() * Short.MAX_VALUE * 0.3);
            default:
                return 0;
        }
    }

    public void stopNoise() {
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
}
