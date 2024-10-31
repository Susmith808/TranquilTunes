package com.example.tranquiltunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;
import java.util.HashMap;

public class Padengine {
    private static final HashMap<String, MediaPlayer> mediaPlayers = new HashMap<>();


    public static void playAudio(Context context, String padname) {
        String audioUrl;

        // Choose audio URL based on atmosname
        switch (padname) {
            case "space":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Faurora.mp3?alt=media&token=b87acdf6-424e-4beb-9a34-57a5f23a82eb";
                break;
            case "lofi":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Faurora.mp3?alt=media&token=b87acdf6-424e-4beb-9a34-57a5f23a82eb";
                break;
            case "mountain":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Faurora.mp3?alt=media&token=b87acdf6-424e-4beb-9a34-57a5f23a82eb";
                break;
            default:
                Toast.makeText(context, "Invalid choice", Toast.LENGTH_SHORT).show();
                return;
        }

        // Check if MediaPlayer for this atmosname already exists
        MediaPlayer mediaPlayer = mediaPlayers.get(padname);
        if (mediaPlayer != null) {
            // If playing, return to avoid reloading
            if (mediaPlayer.isPlaying()) return;
            mediaPlayer.release();  // Release the current instance if not playing
        }

        // Initialize a new MediaPlayer for this atmosname
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            MediaPlayer finalMediaPlayer = mediaPlayer;
            mediaPlayer.setOnPreparedListener(mp -> finalMediaPlayer.start());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
                return true;
            });

            // Store in HashMap
            mediaPlayers.put(padname, mediaPlayer);

        } catch (Exception e) {
            Toast.makeText(context, "Failed to load audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Stop and release all MediaPlayer resources
    public static void stopAudio() {
        for (MediaPlayer mediaPlayer : mediaPlayers.values()) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        mediaPlayers.clear(); // Clear all MediaPlayer instances
    }
}
