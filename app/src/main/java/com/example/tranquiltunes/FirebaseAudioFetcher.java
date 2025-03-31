package com.example.tranquiltunes;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseAudioFetcher {

    public interface AudioFetchListener {
        void onAudioFetched(List<AudioModel> audioList);
        void onFetchError(String error);
    }

    public static void fetchNatureSounds(Context context, AudioFetchListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("natureSounds");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AudioModel> audioList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    AudioModel audioFile = dataSnapshot.getValue(AudioModel.class);
                    if (audioFile != null) {
                        audioList.add(audioFile);
                    }
                }
                listener.onAudioFetched(audioList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFetchError(error.getMessage());
                Toast.makeText(context, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
