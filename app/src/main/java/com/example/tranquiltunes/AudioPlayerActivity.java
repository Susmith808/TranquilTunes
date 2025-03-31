package com.example.tranquiltunes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AudioPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ImageView playButton, setSessionButton, fadeToggleButton, showSessionButton;
    private ProgressBar progressBar;
    private SeekBar volumeSeekBar;
    private LinearLayout sessionLayout;
    private TextView sessionTextView, fadeTextView, volumeTextView;
    private boolean isPlaying = false;
    private boolean fadeEnabled = false;
    private boolean sessionControlsVisible = false;
    private int sessionDuration = 0;
    private Handler handler = new Handler();
    private Runnable sessionRunnable, fadeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);

        // Initialize UI elements
        playButton = findViewById(R.id.playButton);
        setSessionButton = findViewById(R.id.setSessionButton);
        fadeToggleButton = findViewById(R.id.fadeToggleButton);
        showSessionButton = findViewById(R.id.showSessionButton);
        progressBar = findViewById(R.id.progressBar);
        volumeSeekBar = findViewById(R.id.volumeSeekBar);
        sessionLayout = findViewById(R.id.sessionLayout);
        sessionTextView = findViewById(R.id.sessionTextView);
        fadeTextView = findViewById(R.id.fadeTextView);
        volumeTextView = findViewById(R.id.volumeTextView);

        // Hide session controls initially
        sessionLayout.setVisibility(View.GONE);

        String audioUrl = getIntent().getStringExtra("AUDIO_URL");

        if (audioUrl == null || audioUrl.isEmpty()) {
            Toast.makeText(this, "Error: No audio URL provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // MediaPlayer setup
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            progressBar.setVisibility(View.VISIBLE);

            mediaPlayer.setOnPreparedListener(mp -> {
                playButton.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                volumeSeekBar.setProgress(100);
                mediaPlayer.setVolume(1.0f, 1.0f);
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return true;
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
            finish();
        }

        // ImageView Listeners
        playButton.setOnClickListener(v -> togglePlayback());
        setSessionButton.setOnClickListener(v -> setSession());
        fadeToggleButton.setOnClickListener(v -> toggleFadeEffect());
        showSessionButton.setOnClickListener(v -> toggleSessionControls());

        // Volume SeekBar Listener
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float volume = progress / 100f;
                mediaPlayer.setVolume(volume, volume);
                volumeTextView.setText("Volume: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void togglePlayback() {
        if (mediaPlayer != null) {
            if (!isPlaying) {
                mediaPlayer.start();
                playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.pause));
            } else {
                mediaPlayer.pause();
                playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play));
            }
            isPlaying = !isPlaying;
        }
    }

    private void toggleSessionControls() {
        sessionControlsVisible = !sessionControlsVisible;
        sessionLayout.setVisibility(sessionControlsVisible ? View.VISIBLE : View.GONE);
        showSessionButton.setImageDrawable(ContextCompat.getDrawable(this,
                sessionControlsVisible ? R.drawable.headphonelogo : R.drawable.circle_animation));
    }

    private void setSession() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Session Duration");

        // Create a NumberPicker for session duration
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);  // Minimum 1 minute
        numberPicker.setMaxValue(60); // Maximum 60 minutes
        numberPicker.setValue(5);     // Default to 5 minutes

        builder.setView(numberPicker);

        builder.setPositiveButton("Set", (dialog, which) -> {
            int minutes = numberPicker.getValue();
            sessionDuration = minutes * 60 * 1000; // Convert to milliseconds
            sessionTextView.setText("Session Duration: " + minutes + " min");
            Toast.makeText(this, "Session set for " + minutes + " minutes", Toast.LENGTH_SHORT).show();

            if (sessionRunnable != null) {
                handler.removeCallbacks(sessionRunnable);
            }

            sessionRunnable = () -> {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    playButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play));
                    isPlaying = false;
                }
            };

            handler.postDelayed(sessionRunnable, sessionDuration);

            if (fadeEnabled) {
                startFadingEffect();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void toggleFadeEffect() {
        fadeEnabled = !fadeEnabled;
        fadeToggleButton.setImageDrawable(ContextCompat.getDrawable(this,
                fadeEnabled ? R.drawable.roundload : R.drawable.circle_animation));
        fadeTextView.setText("Fade Effect: " + (fadeEnabled ? "On" : "Off"));
        Toast.makeText(this, "Volume Fade " + (fadeEnabled ? "Enabled" : "Disabled"), Toast.LENGTH_SHORT).show();
    }

    private void startFadingEffect() {
        if (fadeRunnable != null) {
            handler.removeCallbacks(fadeRunnable);
        }

        int fadeStartTime = (int) (sessionDuration * 0.8);
        fadeRunnable = new Runnable() {
            float volume = 1.0f;

            @Override
            public void run() {
                if (mediaPlayer.isPlaying() && volume > 0.1f) {
                    volume -= 0.05f;
                    mediaPlayer.setVolume(volume, volume);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(fadeRunnable, fadeStartTime);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (handler != null) {
            handler.removeCallbacks(sessionRunnable);
            handler.removeCallbacks(fadeRunnable);
        }
    }
}
