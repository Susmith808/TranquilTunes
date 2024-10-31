//Atmosengine
package com.example.tranquiltunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;

public class Atmosengine {
    private static MediaPlayer mediaPlayer;

    public static void playAudio(Context context, String atmosname) {
        String audioUrl;

        // Choose audio URL based on atmosname
        if ("forest".equals(atmosname)) {
            audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Fab.mp3?alt=media&token=12779c42-9bb2-4686-87fd-3d76cdf86565";
        } else if ("beach".equals(atmosname)) {
            audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/audiodata%2Fkurd.mp3?alt=media&token=0957156b-44c1-490f-90ce-3fff4ecb102e";
        } else if ("mountain".equals(atmosname)) {
            audioUrl = "https://www.example.com/audio3.mp3";
        } else {
            Toast.makeText(context, "Invalid choice", Toast.LENGTH_SHORT).show();
            return;
        }

        // Release any existing MediaPlayer instance
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        // Initialize and start MediaPlayer
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> mediaPlayer.start());
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
                return true;
            });
        } catch (Exception e) {
            Toast.makeText(context, "Failed to load audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Stop and release MediaPlayer resources
    public static void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
