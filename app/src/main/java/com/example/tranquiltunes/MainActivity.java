package com.example.tranquiltunes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the atmosname passed from Atmosadapter
        Intent intent = getIntent();
        String atmosname = intent.getStringExtra("atmosname"); // Get atmosname as a string

        if (atmosname != null) {
            playAudio(atmosname); // Call playAudio with the received atmosname
        } else {
            Toast.makeText(this, "No atmosphere selected", Toast.LENGTH_SHORT).show();
        }
    }

    // Updated playAudio method to accept atmosname as a String
    private void playAudio(String atmosname) {
        String audioUrl;

        // Choose audio URL based on the atmosname string
        if ("forest".equals(atmosname)) {
            audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Fab.mp3?alt=media&token=12779c42-9bb2-4686-87fd-3d76cdf86565";
        } else if ("beach".equals(atmosname)) {
            audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Fkurd.mp3?alt=media&token=0957156b-44c1-490f-90ce-3fff4ecb102e";
        } else if ("mountain".equals(atmosname)) {
            audioUrl = "https://www.example.com/audio3.mp3";
        } else {
            Toast.makeText(this, "Invalid choice", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null; // Set mediaPlayer to null after releasing
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(this, "Error playing audio", Toast.LENGTH_SHORT).show();
                return true;
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null; // Set mediaPlayer to null after releasing
        }
    }
}
