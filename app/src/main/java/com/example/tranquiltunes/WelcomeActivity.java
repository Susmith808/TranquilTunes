package com.example.tranquiltunes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcomeMessage;
    private MediaPlayer mediaPlayer;
    private StorageReference storageReference;
    private Handler handler = new Handler();
    private int charIndex = 0;
    private Button startButton;
    private Button helpButton;
    private Button bbButton;
    private Button exitButton;  // Declare the exit button
    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Close the app
                    finishAffinity(); // Close all activities in the stack
                    System.exit(0); // Optionally, close the application process
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog and stay in the app
                    dialog.dismiss();
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //2025
         bbButton=findViewById(R.id.binauralBeatsButton);

        // Initialize the TextView and Buttons
        welcomeMessage = findViewById(R.id.welcomeMessage);
        startButton = findViewById(R.id.startbuttonid);
        helpButton = findViewById(R.id.helpbuttonid);
        exitButton = findViewById(R.id.exitButtonid);  // Initialize the exit button

        //2025
        bbButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, BinauralBeatsActivity.class);
            startActivity(intent);
        });

        // Set up the Help button
        helpButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, Help.class);
            startActivity(intent);
        });

        // Set up the Start button to navigate to EmotionActivity
        startButton.setOnClickListener(view -> {
            Intent intent = new Intent(WelcomeActivity.this, Emotion.class);
            startActivity(intent);
        });

        // Set up the Exit button
        exitButton.setOnClickListener(view -> showExitDialog());


        // Get current hour and set the welcome message
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String message;
        if (currentHour >= 0 && currentHour < 12) {
            message = "\nWelcome to TRANQUILTUNES\n\n\tStep into a world of calm and relaxation with TranquilTunes\n\nyour personal ambient music generator.\n\n";
            fetchAudio("audiodata/bootsound2.mp3");
        } else if (currentHour >= 12 && currentHour < 18) {
            message = "\nWelcome to TRANQUILTUNES\n\n\tStep into a world of calm and relaxation with TranquilTunes\n\nyour personal ambient music generator.\n\n";
            fetchAudio("audiodata/bootsound2.mp3");
        } else {
            message = "\nWelcome to TRANQUILTUNES\n\n\tStep into a world of calm and relaxation with TranquilTunes\n\nyour personal ambient music generator.\n\n";
            fetchAudio("audiodata/bootsound2.mp3");
        }

        // Display the message with typewriter effect
        setTypewriterText(welcomeMessage, message);
    }

    // Method to display text with typewriter effect
    private void setTypewriterText(TextView textView, String message) {
        charIndex = 0;
        textView.setText(""); // Clear the text view initially

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (charIndex < message.length()) {
                    textView.append(String.valueOf(message.charAt(charIndex)));
                    charIndex++;
                    handler.postDelayed(this, 50); // Adjust delay for typing speed
                }
            }
        }, 100);
    }

    // Method to fetch audio file from Firebase Storage
    private void fetchAudio(String audioPath) {
        StorageReference audioRef = storageReference.child(audioPath);

        audioRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Once the download URL is fetched, play the audio
            playAudio(uri.toString());
        }).addOnFailureListener(e -> {
            Log.e("FirebaseAudio", "Error fetching audio: " + e.getMessage());
            Toast.makeText(WelcomeActivity.this, "Failed to fetch audio", Toast.LENGTH_SHORT).show();
        });
    }

    // Method to play the fetched audio
    private void playAudio(String audioUrl) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(WelcomeActivity.this, "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
