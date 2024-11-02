package com.example.tranquiltunes;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_STREAMS = 3; // Maximum concurrent streams
    private ArrayList<String> noteUrls = new ArrayList<>();
    private ArrayList<MediaPlayer> activePlayers = new ArrayList<>(); // Track active MediaPlayer instances
    private Handler handler = new Handler();
    private Random random = new Random();
    private String selectedEmotion;
    private String selectedInstrument;

    private Button playButton;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = findViewById(R.id.button);

        // Retrieve emotion and instrument from GlobalData (or SharedPreferences as needed)
        selectedEmotion = GlobalData.getInstance().getSelectedEmotion();
        selectedInstrument = GlobalData.getInstance().getSelectedInstrument();

        // Fetch note URLs from Firebase Storage
        fetchNoteUrlsFromStorage();

        // Retrieve atmos name and pad name from SharedPreferences
        SharedPreferences sharedATMOSPreferences = getSharedPreferences("AtmosPreferences", MODE_PRIVATE);
        String atmosName = sharedATMOSPreferences.getString("selectedAtmosname", "defaultAtmos");

        SharedPreferences sharedPADSPreferences = getSharedPreferences("PadsPreferences", MODE_PRIVATE);
        String padName = sharedPADSPreferences.getString("selectedpadName", "defaultPads");

        // Set up play button to play audio from Atmosengine, Padengine, and Firebase notes
        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlayback();
            } else {
                Atmosengine.playAudio(this, atmosName);
                Padengine.playAudio(this, padName);
                startPlayback();
            }
        });
    }

    private void fetchNoteUrlsFromStorage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference()
                .child("notes")
                .child(selectedEmotion)
                .child(selectedInstrument);

        storageRef.listAll().addOnSuccessListener(listResult -> {
            noteUrls.clear();

            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    noteUrls.add(uri.toString());
                    if (noteUrls.size() == listResult.getItems().size()) {
                        Toast.makeText(this, "Notes loaded successfully.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load notes.", Toast.LENGTH_SHORT).show()
        );
    }

    public void startPlayback() {
        if (!noteUrls.isEmpty()) {
            isPlaying = true;
            playButton.setText("Stop");
            playNextNoteWithTimedDelays();
        } else {
            Toast.makeText(this, "No notes available to play.", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopPlayback() {
        isPlaying = false;
        playButton.setText("Play");
        handler.removeCallbacksAndMessages(null);

        // Stop and release all active MediaPlayers
        for (MediaPlayer player : activePlayers) {
            if (player.isPlaying()) player.stop();
            player.release();
        }
        activePlayers.clear();

        // Stop Atmosengine and Padengine audio
        Atmosengine.stopAudio();
        Padengine.stopAudio();
    }

    private void playNextNoteWithTimedDelays() {
        if (!isPlaying || noteUrls.isEmpty() || activePlayers.size() >= MAX_STREAMS) return;

        int[] presetDelays = {1000, 2000, 500};
        int chosenDelay = presetDelays[random.nextInt(presetDelays.length)];

        String url = noteUrls.get(random.nextInt(noteUrls.size()));
        Uri uri = Uri.parse(url);

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setOnPreparedListener(mp -> mp.start());
            mediaPlayer.setOnCompletionListener(mp -> {
                activePlayers.remove(mp); // Remove from active list when playback is complete
                mp.release();
                if (isPlaying) {
                    playNextNoteWithTimedDelays(); // Continue with the next note if still playing
                }
            });
            mediaPlayer.prepareAsync();

            activePlayers.add(mediaPlayer); // Add to active list after preparation

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading audio.", Toast.LENGTH_SHORT).show();
        }

        handler.postDelayed(this::playNextNoteWithTimedDelays, chosenDelay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayback(); // Stop playback and release resources when the activity is destroyed
    }
}
