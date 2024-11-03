package com.example.tranquiltunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;
import java.util.HashMap;

public class Atmosengine {
    private static final HashMap<String, MediaPlayer> mediaPlayers = new HashMap<>();

    // New method to get the audio URL based on atmosphere name
    public static String getAudioUrl(String atmosname) {

        switch (atmosname) {
            case "rain":
                return "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2FRain%20Sound%20Effect%20Rain%20Sounds%20No%20copyright%20sound%20Effect.mp3?alt=media&token=30d7f964-49e2-41eb-bbf9-68f01d78074f";
            case "woods":
                return "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2F1%20minute%20relaxing%20soundscape%20_%20woodland%20chorus.mp3?alt=media&token=7334755a-a867-4433-a069-a3fd5cb67054";
            case "mountain":
                return "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Fparanoid.mp3?alt=media&token=06d64c53-574f-4e0c-84eb-c8769e6969dc";
            default:
                return null; // Return null if the atmosphere name is invalid
        }
    }

    public static void playAudio(Context context, String atmosname) {
        String audioUrl = getAudioUrl(atmosname); // Use the new method to get the URL

        if (audioUrl == null) {
            Toast.makeText(context, "Invalid choice", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if MediaPlayer for this atmosname already exists
        MediaPlayer mediaPlayer = mediaPlayers.get(atmosname);
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
            mediaPlayers.put(atmosname, mediaPlayer);

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
