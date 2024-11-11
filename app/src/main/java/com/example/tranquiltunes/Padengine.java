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
            case "happy1":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%201-Kontakt.mp3?alt=media&token=d550bf37-1a6f-4341-93db-50c1372f9b3d";
                break;
            case "happy2":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%202-Kontakt.mp3?alt=media&token=3cf016db-9a37-405c-8b3d-68fc78955157";
                break;
            case "happy3":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%203-Kontakt.mp3?alt=media&token=5f44e0bb-ee22-4ce0-891b-0265db5961b9";
                break;
            case "happy4":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%204-Kontakt.mp3?alt=media&token=b636ca95-d46a-426c-a934-7a25c54d7e1e";
                break;
            case "happy5":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%205-Kontakt.mp3?alt=media&token=6e538231-79e4-4b95-a85f-95a2accfb8bb";
                break;
//calm
            case "inspired1":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%201-Kontakt.mp3?alt=media&token=62afa2c2-5f6c-47c2-8845-e7967f46207c";
                break;
            case "inspired2":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%202-Kontakt.mp3?alt=media&token=5a965b93-42af-445a-9dfc-51f5f4c174a4";
                break;
            case "inspired3":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%203-Kontakt.mp3?alt=media&token=8b327e4b-01f6-4d77-96a9-d15e81c9c10f";
                break;
            case "inspired4":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%204-Kontakt.mp3?alt=media&token=5134f1a5-bd43-4010-8e38-442be8b4d543";
                break;
            case "inspired5":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%205-Kontakt.mp3?alt=media&token=c2b94dc2-355f-497d-9087-ac7a57e4a24a";
                break;

//calm
            case "calm1":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%201-Kontakt.mp3?alt=media&token=49f7c17f-07b7-47fa-b8ca-eabbbf425944";
                break;
            case "calm2":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%202-Kontakt.mp3?alt=media&token=68039840-a04c-4d84-961c-27ae905647df";
                break;
            case "calm3":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%203-Kontakt.mp3?alt=media&token=60e1ec11-2d88-4472-b26f-22fe3f7cbc54";
                break;
            case "calm4":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%204-Kontakt.mp3?alt=media&token=f49e57da-8a5c-423e-a12a-6d991afe74e4";
                break;
            case "calm5":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%205-Kontakt.mp3?alt=media&token=66cc6875-3a18-47d6-bddd-eb1e99b54dcd";
                break;

//calm
            case "hopeful1":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%201-Kontakt.mp3?alt=media&token=53b5b631-d084-4cda-b5d5-96c705efdc8e";
                break;
            case "hopeful2":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%202-Kontakt.mp3?alt=media&token=2bbf5ced-f5a9-4068-9101-a72c5fc95469";
                break;
            case "hopeful3":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%203-Kontakt.mp3?alt=media&token=a2a708d4-5885-41e7-b900-aca9e79225d5";
                break;
            case "hopeful4":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%204-Kontakt.mp3?alt=media&token=09705ba0-4ee3-4972-891f-03a8d948fef4";
                break;
            case "hopeful5":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%205-Kontakt.mp3?alt=media&token=4265b54a-775d-4484-b59f-2b95df71d8e8";
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
