package com.example.tranquiltunes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class Engine {

    private Context context;
    private MediaPlayer mediaPlayer;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String audioUrl;

    public Engine(Context context) {
        this.context = context;
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("");

        fetchAudioUrl();
    }

    private void fetchAudioUrl() {
        // Fetch the audio URL from Firebase Storage
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                audioUrl = uri.toString();
                Toast.makeText(context, "Audio ready to play", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Failed to get audio URL", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playAudio() {
        if (audioUrl == null) {
            Toast.makeText(context, "Audio URL is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare(); // might take time depending on the file size
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show();
        }
    }

    public void releasePlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
