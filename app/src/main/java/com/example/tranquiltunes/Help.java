package com.example.tranquiltunes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;

public class Help extends AppCompatActivity {

    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Set up the toolbar as the action bar with back button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Enable back button
        }

        // Set the help text
        TextView helpTextView = findViewById(R.id.helpTextView);
        helpTextView.setText(getHelpText());  // Display the help text
        helpTextView.setMovementMethod(new ScrollingMovementMethod()); // Make it scrollable

        // Initialize MediaPlayer for each audio file
        mediaPlayer1 = MediaPlayer.create(this, R.raw.padexample); // Place audio1.mp3 in res/raw
        mediaPlayer2 = MediaPlayer.create(this, R.raw.atmosexample); // Place audio2.mp3 in res/raw

        // Set up button to play first audio file
        Button buttonPlayAudio1 = findViewById(R.id.button4);
        buttonPlayAudio1.setOnClickListener(v -> playAudio(mediaPlayer1));

        // Set up button to play second audio file
        Button buttonPlayAudio2 = findViewById(R.id.button5);
        buttonPlayAudio2.setOnClickListener(v -> playAudio(mediaPlayer2));
    }

    // Method to play audio
    private void playAudio(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause(); // Pause if already playing
            mediaPlayer.seekTo(0); // Reset to start
        }
        mediaPlayer.start(); // Start playback
    }

    // Method to get help text
    private String getHelpText() {
        return "Welcome to TRANQUILTUNES!\n\n" +
                "This app allows you to generate relaxing ambient music based on your chosen Emotion/Mood, Atmosphere, Musicscape, and Musical Instrument.\n\n\n\n" +
                "HOW TO USE:\n\n" +
                "\t1. SELECT THE MOOD/EMOTION: Choose from emotions like Calm, Inspired, Hopeful, etc.\n\n" +
                "\t2. CHOOSE AN ATMOSPHERE: Set the Atmosphere for your experience.\n\n" +
                "\t3. CHOOSE MUSICSCAPE: Pick the type of Musicscape you prefer.\n\n" +
                "\t4. CHOOSE MUSICAL INSTRUMENT: Pick the Musical Instrument you prefer.\n\n" +
                "\t5. ENJOY THE AMBIENT MUSIC\n" +
                "\t__________________________________________________\n\n" +
                "Contextual Meanings:\n\n" +
                "MUSICSCAPE: Soft, sustained humming-like sounds that serve as a background texture, creating a smooth, flowing foundation in ambient music.\n\n" +
                "ATMOSPHERE: Environmental sounds, effects, and ambient subtle sounds like flowing water, wind, soft rain, distant echoes, or nature sounds that make the music feel immersive and transportive.\n\n" +
                "EXAMPLE:-\n";
    }

    // Handle the back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Navigate back to WelcomeActivity
            Intent intent = new Intent(Help.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources to prevent memory leaks
        if (mediaPlayer1 != null) {
            mediaPlayer1.release();
            mediaPlayer1 = null;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }
}
