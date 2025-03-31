package com.example.tranquiltunes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SoundManager extends AppCompatActivity {

    private Button natureSoundsButton, ambientMusicButton;
    private Button natureToggleButton, ambientToggleButton;
    private MediaPlayer natureMediaPlayer, ambientMediaPlayer;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    protected void initializeSoundControls(Button natureBtn, Button ambientBtn, Button natureToggle, Button ambientToggle) {
        this.natureSoundsButton = natureBtn;
        this.ambientMusicButton = ambientBtn;
        this.natureToggleButton = natureToggle;
        this.ambientToggleButton = ambientToggle;

        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading sounds...");
        progressDialog.setCancelable(false);

        // Hide toggle buttons initially
        natureToggleButton.setVisibility(Button.GONE);
        ambientToggleButton.setVisibility(Button.GONE);

        // Fetch and play sounds
        natureSoundsButton.setOnClickListener(v -> fetchSoundsFromFirebase("natureSounds", true));
        ambientMusicButton.setOnClickListener(v -> fetchSoundsFromFirebase("ambientMusic", false));

        // Toggle Buttons for Nature and Ambient Sounds
        natureToggleButton.setOnClickListener(v -> handleToggle(natureToggleButton, true));
        ambientToggleButton.setOnClickListener(v -> handleToggle(ambientToggleButton, false));
    }

    private void fetchSoundsFromFirebase(String category, boolean isNatureSound) {
        progressDialog.show();
        databaseReference.child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<String> soundList = new ArrayList<>();
                List<String> soundUrls = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String name = data.child("name").getValue(String.class);
                    String url = data.child("url").getValue(String.class);
                    if (name != null && url != null) {
                        soundList.add(name);
                        soundUrls.add(url);
                    }
                }
                progressDialog.dismiss();
                showSoundPickerDialog(soundList, soundUrls, isNatureSound);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.dismiss();
                Log.e("Firebase", "Error fetching sounds", error.toException());
            }
        });
    }

    private void showSoundPickerDialog(List<String> soundList, List<String> soundUrls, boolean isNatureSound) {
        new AlertDialog.Builder(this)
                .setTitle("Select a Sound")
                .setItems(soundList.toArray(new String[0]), (dialog, which) -> {
                    String selectedSoundUrl = soundUrls.get(which);
                    playSelectedAudio(selectedSoundUrl, isNatureSound);
                })
                .show();
    }

    private void playSelectedAudio(String audioUrl, boolean isNatureSound) {
        MediaPlayer player = isNatureSound ? natureMediaPlayer : ambientMediaPlayer;

        if (player != null) {
            player.stop();
            player.release();
        }

        player = new MediaPlayer();
        try {
            player.setDataSource(audioUrl);
            player.setLooping(true);
            player.prepareAsync();
            player.setOnPreparedListener(mp -> {
                mp.start();
                if (isNatureSound) {
                    natureMediaPlayer = mp;
                    natureToggleButton.setVisibility(View.VISIBLE);
                    natureToggleButton.setText("Pause Nature Sound");
                } else {
                    ambientMediaPlayer = mp;
                    ambientToggleButton.setVisibility(View.VISIBLE);
                    ambientToggleButton.setText("Pause Ambient Music");
                }
            });
        } catch (IOException e) {
            Log.e("Audio", "Error playing audio", e);
            Toast.makeText(this, "Unable to play audio", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleToggle(Button button, boolean isNatureSound) {
        MediaPlayer player = isNatureSound ? natureMediaPlayer : ambientMediaPlayer;

        if (player == null) {
            Toast.makeText(this, "Please select a sound first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!player.isPlaying()) {
            player.start();
            button.setText("Pause");
        } else {
            player.pause();
            button.setText("Resume");
        }
    }
}
