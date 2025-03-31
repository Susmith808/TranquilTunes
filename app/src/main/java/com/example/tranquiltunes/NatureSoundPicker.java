package com.example.tranquiltunes;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class NatureSoundPicker {

    private final Context context;
    private final DatabaseReference databaseReference;
    private final FirebaseStorage storage;
    private MediaPlayer mediaPlayer;

    public NatureSoundPicker(Context context) {
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("nature_sounds");
        this.storage = FirebaseStorage.getInstance();
    }

    public void showSoundPickerDialog() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                List<String> soundNames = new ArrayList<>();
                List<String> soundUrls = new ArrayList<>();

                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String name = snapshot.getKey();
                    String url = snapshot.getValue(String.class);
                    if (name != null && url != null) {
                        soundNames.add(name);
                        soundUrls.add(url);
                    }
                }

                if (!soundNames.isEmpty()) {
                    showDialog(soundNames, soundUrls);
                } else {
                    Toast.makeText(context, "No sounds available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to fetch sounds", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialog(List<String> soundNames, List<String> soundUrls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a Nature Sound");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, soundNames);
        builder.setAdapter(adapter, (dialog, which) -> {
            String selectedUrl = soundUrls.get(which);
            playSound(selectedUrl);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void playSound(String soundUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(soundUrl));
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnCompletionListener(mp -> mp.release());
        } catch (Exception e) {
            Toast.makeText(context, "Error playing sound", Toast.LENGTH_SHORT).show();
        }
    }
}
