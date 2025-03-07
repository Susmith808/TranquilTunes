package com.example.tranquiltunes;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_STREAMS = 3;
    private ArrayList<String> noteUrls = new ArrayList<>();
    private ArrayList<MediaPlayer> activePlayers = new ArrayList<>();
    private Handler handler = new Handler();
    private Random random = new Random();
    private String selectedEmotion;
    private String selectedInstrument;

    private Button playButton;
    private Button buttonActivity1, buttonActivity2, buttonActivity3,buttonActivity4,exitbutton; // Buttons for navigating to activities
    private boolean isPlaying = false;

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
        setContentView(R.layout.activity_main);




        playButton = findViewById(R.id.button);
        buttonActivity1 = findViewById(R.id.changeemo);
        buttonActivity2 = findViewById(R.id.changepad);
        buttonActivity3 = findViewById(R.id.changeatmos);
        buttonActivity4 = findViewById(R.id.changeinst);
        exitbutton = findViewById(R.id.exit);

        selectedEmotion = GlobalData.getInstance().getSelectedEmotion();
        selectedInstrument = GlobalData.getInstance().getSelectedInstrument();

        fetchNoteUrlsFromStorage();

        SharedPreferences sharedATMOSPreferences = getSharedPreferences("AtmosPreferences", MODE_PRIVATE);
        String atmosName = sharedATMOSPreferences.getString("selectedAtmosname", "defaultAtmos");

        SharedPreferences sharedPADSPreferences = getSharedPreferences("PadsPreferences", MODE_PRIVATE);
        String padName = sharedPADSPreferences.getString("selectedpadName", "defaultPads");

        playButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopPlayback();
            } else {
                Atmosengine.playAudio(this, atmosName);
                Padengine.playAudio(this, padName);
                startPlayback();
            }
        });




        // Set up button click listeners for the three new buttons
        buttonActivity1.setOnClickListener(v -> {
            stopPlayback();
            Intent intent = new Intent(MainActivity.this, Emotion.class);
            startActivity(intent);
        });

        buttonActivity2.setOnClickListener(v -> {
            stopPlayback();
            Intent intent = new Intent(MainActivity.this, Pad.class);
            startActivity(intent);
        });

        buttonActivity3.setOnClickListener(v -> {
            stopPlayback();
            Intent intent = new Intent(MainActivity.this, Soundscape.class);
            startActivity(intent);
        });

        buttonActivity4.setOnClickListener(v -> {
            stopPlayback();
            Intent intent = new Intent(MainActivity.this, Instrument.class);
            startActivity(intent);
        });


        exitbutton.setOnClickListener(view -> showExitDialog());






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
                        Toast.makeText(this, "Ambient Music generated successfully.", Toast.LENGTH_SHORT).show();
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

        for (MediaPlayer player : activePlayers) {
            if (player.isPlaying()) player.stop();
            player.release();
        }
        activePlayers.clear();

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
                activePlayers.remove(mp);
                mp.release();
                if (isPlaying) {
                    playNextNoteWithTimedDelays();
                }
            });
            mediaPlayer.prepareAsync();

            activePlayers.add(mediaPlayer);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading audio.", Toast.LENGTH_SHORT).show();
        }

        handler.postDelayed(this::playNextNoteWithTimedDelays, chosenDelay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }
}
