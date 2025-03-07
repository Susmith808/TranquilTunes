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
            case "Celestial Glow":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%201-Kontakt.mp3?alt=media&token=d550bf37-1a6f-4341-93db-50c1372f9b3d";
                break;
            case "Aurora Flow":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%202-Kontakt.mp3?alt=media&token=3cf016db-9a37-405c-8b3d-68fc78955157";
                break;
            case "Zen Echoes":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%203-Kontakt.mp3?alt=media&token=5f44e0bb-ee22-4ce0-891b-0265db5961b9";
                break;
            case "Buddha’s Breath":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%204-Kontakt.mp3?alt=media&token=b636ca95-d46a-426c-a934-7a25c54d7e1e";
                break;
            case "Nuclear Reverie":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhappy%2Ffreecompress-Pads(Happy)%205-Kontakt.mp3?alt=media&token=6e538231-79e4-4b95-a85f-95a2accfb8bb";
                break;
//calm
            case "Cosmic Drift":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%201-Kontakt.mp3?alt=media&token=49f7c17f-07b7-47fa-b8ca-eabbbf425944";
                break;
            case "Arctic Glow":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%202-Kontakt.mp3?alt=media&token=68039840-a04c-4d84-961c-27ae905647df";
                break;
            case "Himalayan Wind":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%203-Kontakt.mp3?alt=media&token=60e1ec11-2d88-4472-b26f-22fe3f7cbc54";
                break;
            case "Whispering Monks":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%204-Kontakt.mp3?alt=media&token=f49e57da-8a5c-423e-a12a-6d991afe74e4";
                break;
            case "Echoes of the End":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fcalm%2Ffreecompress-Pads(Calm)%205-Kontakt.mp3?alt=media&token=66cc6875-3a18-47d6-bddd-eb1e99b54dcd";
                break;

//inspired
            case "Meteor Glow":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%201-Kontakt.mp3?alt=media&token=62afa2c2-5f6c-47c2-8845-e7967f46207c";
                break;
            case "Glacial Aura":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%202-Kontakt.mp3?alt=media&token=5a965b93-42af-445a-9dfc-51f5f4c174a4";
                break;
            case "Mystic Chimes":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%203-Kontakt.mp3?alt=media&token=8b327e4b-01f6-4d77-96a9-d15e81c9c10f";
                break;
            case "Monk’s Solitude":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%204-Kontakt.mp3?alt=media&token=5134f1a5-bd43-4010-8e38-442be8b4d543";
                break;
            case "Ashes Drift":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Finspired%2Ffreecompress-Pads(Inspired)%205-Kontakt.mp3?alt=media&token=c2b94dc2-355f-497d-9087-ac7a57e4a24a";
                break;

//hopeful
            case "Infinite Void":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%201-Kontakt.mp3?alt=media&token=53b5b631-d084-4cda-b5d5-96c705efdc8e";
                break;
            case "Silent Tundra":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%202-Kontakt.mp3?alt=media&token=2bbf5ced-f5a9-4068-9101-a72c5fc95469";
                break;
            case "Monk’s Chant":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%203-Kontakt.mp3?alt=media&token=a2a708d4-5885-41e7-b900-aca9e79225d5";
                break;
            case "Celestial Chant":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%204-Kontakt.mp3?alt=media&token=09705ba0-4ee3-4972-891f-03a8d948fef4";
                break;
            case "Lost Civilization":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fhopeful%2Ffreecompress-Pads(hopeful)%205-Kontakt.mp3?alt=media&token=4265b54a-775d-4484-b59f-2b95df71d8e8";
                break;

//euphoria
            case "Plasma Horizon":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Feuphoria%2Ffreecompress-Pads(Lonely)%201-Kontakt.mp3?alt=media&token=f435f49a-404b-4ba3-a291-143c71788acb";
                break;
            case "Northern Hues":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Feuphoria%2Ffreecompress-Pads(Lonely)%202-Kontakt.mp3?alt=media&token=f1cc6ad8-be42-4389-90c2-4e2cb4fb1b51";
                break;
            case "Eternal Om":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Feuphoria%2Ffreecompress-Pads(Lonely)%203-Kontakt.mp3?alt=media&token=5acc6f3a-8b81-4ca4-a61c-f346c6f6af44";
                break;
            case "Chanting Souls":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Feuphoria%2Ffreecompress-Pads(Lonely)%204-Kontakt.mp3?alt=media&token=fa12fa2a-9516-4979-a321-d24a2431facb";
                break;
            case "Silent Wasteland":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Feuphoria%2Ffreecompress-Pads(Lonely)%205-Kontakt.mp3?alt=media&token=1a8c6c8b-49df-413c-b554-ff3110e7bed0";
                break;

//emptiness
            case "Astral Flow":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Femptiness%2Ffreecompress-Pads(Emptiness)%201-Kontakt.mp3?alt=media&token=f8ee746c-8664-4d21-8b27-54844187f9a1";
                break;
            case "Icy Reverie":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Femptiness%2Ffreecompress-Pads(Emptiness)%202-Kontakt.mp3?alt=media&token=d736d0e5-23e7-4c9b-a792-8a34b7116215";
                break;
            case "Monastic Whisper":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Femptiness%2Ffreecompress-Pads(Emptiness)%203-Kontakt.mp3?alt=media&token=a066582f-3205-41f6-82e0-e5acb561bd4b";
                break;
            case "Monastic Aura":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Femptiness%2Ffreecompress-Pads(Emptiness)%204-Kontakt.mp3?alt=media&token=df9bc658-d3d8-41e8-9b5d-5be5c07f1eb0";
                break;
            case "Ruined Dawn":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Femptiness%2Ffreecompress-Pads(Emptiness)%205-Kontakt.mp3?alt=media&token=a5b98fba-8255-4122-b72a-efd1c161518f";
                break;


//despair
            case "Celestial Pulse":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fdespair%2Ffreecompress-Pads(Despair)%201-Kontakt.mp3?alt=media&token=e4c04ec8-bbfa-420c-a04c-c94f27581bd8";
                break;
            case "Snowlit Horizon":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fdespair%2Ffreecompress-Pads(Despair)%202-Kontakt.mp3?alt=media&token=d43b664a-3b25-42e8-945d-b300ae19adef";
                break;
            case "Path to Nirvana":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fdespair%2Ffreecompress-Pads(Despair)%203-Kontakt.mp3?alt=media&token=d71ca6de-d63e-44cf-a606-6b1f51ddc644";
                break;
            case "Divine Chant":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fdespair%2Ffreecompress-Pads(Despair)%204-Kontakt.mp3?alt=media&token=91d61dda-ff4c-48ee-a771-425c4def681a";
                break;
            case "Decay Pulse":
                audioUrl = "https://firebasestorage.googleapis.com/v0/b/tt-amb-gen.appspot.com/o/pads%2Fdespair%2Ffreecompress-Pads(Despair)%205-Kontakt.mp3?alt=media&token=73d217d3-c743-413d-bc57-24d681a7ea24";
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
