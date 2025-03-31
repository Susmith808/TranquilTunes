package com.example.tranquiltunes;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class IsochronicToneGenerator {
    private static final int SAMPLE_RATE = 44100;
    private AudioTrack audioTrack;
    private Thread soundThread;
    private boolean isPlaying = false;
    private double currentVolume = 0.5;
    private double currentPulseRate = 10.0; // Default pulse rate

    public void playIsochronicTone(final double toneFrequency, double pulseRate, final double volume) {
        stopIsochronicTone(); // Stop existing playback before starting
        isPlaying = true;
        currentVolume = volume;
        currentPulseRate = pulseRate; // Store initial pulse rate

        soundThread = new Thread(() -> {
            int bufferSize = AudioTrack.getMinBufferSize(
                    SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            short[] buffer = new short[bufferSize];

            audioTrack = new AudioTrack(
                    AudioManager.STREAM_MUSIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STREAM
            );

            audioTrack.play();

            double phase = 0;
            double pulsePhase = 0;
            double phaseIncrement = 2 * Math.PI * toneFrequency / SAMPLE_RATE;

            while (isPlaying) {
                double pulseIncrement = 2 * Math.PI * currentPulseRate / SAMPLE_RATE; // Update dynamically

                for (int i = 0; i < bufferSize && isPlaying; i++) {
                    // Generate sine wave tone
                    double sineWave = Math.sin(phase);
                    phase += phaseIncrement;
                    if (phase > 2 * Math.PI) phase -= 2 * Math.PI;

                    // Smooth Pulse Modulation
                    double pulse = (Math.sin(pulsePhase) + 1) / 2; // Converts from -1..1 to 0..1
                    pulsePhase += pulseIncrement;
                    if (pulsePhase > 2 * Math.PI) pulsePhase -= 2 * Math.PI;

                    // Final waveform with volume control
                    double sample = sineWave * pulse * currentVolume;
                    buffer[i] = (short) (Math.min(Math.max(sample, -1), 1) * Short.MAX_VALUE);
                }

                audioTrack.write(buffer, 0, bufferSize);
            }
        });

        soundThread.start();
    }

    public void stopIsochronicTone() {
        isPlaying = false;

        if (soundThread != null) {
            try {
                soundThread.join(); // Ensure the thread stops before releasing resources
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            soundThread = null;
        }

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }
    }

    public void updatePulseRate(double newPulseRate) {
        currentPulseRate = newPulseRate; // Update pulse rate dynamically
    }

    public void updateVolume(double volume) {
        currentVolume = volume; // Allows real-time volume adjustment
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}
