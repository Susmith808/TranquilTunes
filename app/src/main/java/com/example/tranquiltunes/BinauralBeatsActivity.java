package com.example.tranquiltunes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BinauralBeatsActivity extends AppCompatActivity {

    private AudioTrack audioTrack;
    private MediaPlayer natureMediaPlayer, ambientMediaPlayer;
    private boolean isPlaying = false;
    private int binauralFrequency;
    private int carrierFrequency = 300;
    private int sessionDuration = 0;
    private Handler sessionHandler = new Handler();
    private Runnable sessionRunnable;

    private SeekBar beatSeekBar;
    private TextView beatFreqText, stateText, sessionDurationText;
    private ImageView playGifButton, setDurationGifButton, playNatureGifButton, playAmbientGifButton;
    private ImageView  natureToggleButton, ambientToggleButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    private Map<String, String> audioMap = new HashMap<>();
    private static final Map<String, int[]> frequencyRangeMap = new HashMap<>();

    static {
        frequencyRangeMap.put("HIGH_STRESS", new int[]{1, 4});
        frequencyRangeMap.put("MODERATE_ANXIETY", new int[]{4, 8});
        frequencyRangeMap.put("SLIGHTLY_STRESSED", new int[]{8, 14});
        frequencyRangeMap.put("RELAXED", new int[]{14, 30});
        frequencyRangeMap.put("SEVERE_DISTRESS", new int[]{30, 50});
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binaural_beats);

        stateText = findViewById(R.id.stateText);
        beatSeekBar = findViewById(R.id.beatSeekBar);
        beatFreqText = findViewById(R.id.beatFreqText);
        sessionDurationText = findViewById(R.id.sessionDurationText);
        setDurationGifButton = findViewById(R.id.setDurationButton);
        playNatureGifButton = findViewById(R.id.selectSoundButton);
        playGifButton = findViewById(R.id.playButton);

        playAmbientGifButton = findViewById(R.id.playAmbientButton);
        natureToggleButton = findViewById(R.id.NatureToggleBtn);
        ambientToggleButton = findViewById(R.id.AmbientToggleBtn);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading sounds...");
        progressDialog.setCancelable(false);

        natureToggleButton.setVisibility(ImageView.GONE);
        ambientToggleButton.setVisibility(ImageView.GONE);

        String psychologicalState = getIntent().getStringExtra("STATE");
        int[] freqRange = frequencyRangeMap.getOrDefault(psychologicalState, new int[]{8, 14});
        int minBeat = freqRange[0];
        int maxBeat = freqRange[1];

        binauralFrequency = (minBeat + maxBeat) / 2;
        beatSeekBar.setMax(maxBeat - minBeat);
        beatSeekBar.setProgress(binauralFrequency - minBeat);
        beatFreqText.setText("Beat Frequency: " + binauralFrequency + " Hz");
        stateText.setText("State: " + psychologicalState);

        beatSeekBar.setOnSeekBarChangeListener(createSeekBarListener(minBeat));

        playGifButton.setOnClickListener(v -> {
            if (isPlaying) {
                stopBinauralBeats();
            } else {
                startBinauralBeats();
            }
        });

        setDurationGifButton.setOnClickListener(v -> showSessionDurationDialog());
        playNatureGifButton.setOnClickListener(v -> fetchSoundsFromFirebase("natureSounds", true));
        playAmbientGifButton.setOnClickListener(v -> fetchSoundsFromFirebase("ambientMusic", false));

        natureToggleButton.setOnClickListener(v -> handleToggle(natureToggleButton, true));
        ambientToggleButton.setOnClickListener(v -> handleToggle(ambientToggleButton, false));
    }

    private void showSessionDurationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Session Duration (in minutes)");

        // Inflate the dialog layout
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60); // Maximum session duration is 60 minutes
        numberPicker.setValue(sessionDuration > 0 ? sessionDuration : 10); // Default to 10 minutes

        builder.setView(numberPicker);

        builder.setPositiveButton("Set", (dialog, which) -> {
            sessionDuration = numberPicker.getValue();
            sessionDurationText.setText("Session Duration: " + sessionDuration + " minutes");
            Toast.makeText(this, "Duration set to " + sessionDuration + " minutes", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }


    private void stopBinauralBeats() {
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }

        isPlaying = false;
        playGifButton.setImageResource(R.drawable.logott);
        Toast.makeText(this, "Binaural beats stopped", Toast.LENGTH_SHORT).show();

        // Remove any pending stop callbacks
        if (sessionRunnable != null) {
            sessionHandler.removeCallbacks(sessionRunnable);
        }
    }

    private void startBinauralBeats() {
        int sampleRate = 44100; // Standard sample rate
        int duration = sessionDuration > 0 ? sessionDuration * 60 : 60; // Default to 60 seconds if not set
        int numSamples = sampleRate * duration;
        double[] leftWave = new double[numSamples];
        double[] rightWave = new double[numSamples];
        short[] buffer = new short[numSamples * 2];

        double beatFrequency = (double) binauralFrequency;
        double carrierFrequencyDouble = (double) carrierFrequency;

        for (int i = 0; i < numSamples; i++) {
            double time = (double) i / sampleRate;
            leftWave[i] = Math.sin(2.0 * Math.PI * carrierFrequencyDouble * time);
            rightWave[i] = Math.sin(2.0 * Math.PI * (carrierFrequencyDouble + beatFrequency) * time);
        }

        for (int i = 0, j = 0; i < numSamples; i++) {
            buffer[j++] = (short) (leftWave[i] * Short.MAX_VALUE);
            buffer[j++] = (short) (rightWave[i] * Short.MAX_VALUE);
        }

        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                buffer.length * 2,
                AudioTrack.MODE_STATIC
        );

        audioTrack.write(buffer, 0, buffer.length);
        audioTrack.play();
        isPlaying = true;

        playGifButton.setImageResource(R.drawable.pause);
        Toast.makeText(this, "Binaural beats started", Toast.LENGTH_SHORT).show();

        // Stop the session after the specified duration
        if (sessionDuration > 0) {
            sessionRunnable = () -> stopBinauralBeats();
            sessionHandler.postDelayed(sessionRunnable, sessionDuration * 60 * 1000);
        }
    }


    private SeekBar.OnSeekBarChangeListener createSeekBarListener(int minValue) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binauralFrequency = minValue + progress;
                beatFreqText.setText("Beat Frequency: " + binauralFrequency + " Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };
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
                Toast.makeText(BinauralBeatsActivity.this, "Error fetching sounds.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSoundPickerDialog(List<String> soundList, List<String> soundUrls, boolean isNatureSound) {
        new AlertDialog.Builder(this)
                .setTitle(isNatureSound ? "Select Nature Sound" : "Select Ambient Music")
                .setItems(soundList.toArray(new String[0]), (dialog, which) -> {
                    String selectedSoundUrl = soundUrls.get(which);
                    playSelectedAudio(selectedSoundUrl, isNatureSound);
                    if (isNatureSound) {
                        natureToggleButton.setVisibility(ImageView.VISIBLE);
                    } else {
                        ambientToggleButton.setVisibility(ImageView.VISIBLE);
                    }
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
                Toast.makeText(BinauralBeatsActivity.this, "Sound ready. Use the toggle button to play.", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isNatureSound) {
            natureMediaPlayer = player;
        } else {
            ambientMediaPlayer = player;
        }
    }

    private void handleToggle(ImageView button, boolean isNatureSound) {
        MediaPlayer player = isNatureSound ? natureMediaPlayer : ambientMediaPlayer;

        if (player == null) {
            Toast.makeText(this, "Please select a sound first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!player.isPlaying()) {
            player.start();
            button.setImageResource(isNatureSound ? R.drawable.circle_animation : R.drawable.headphonelogo);
        } else {
            player.pause();
            button.setImageResource(isNatureSound ? R.drawable.headphonelogo : R.drawable.circle_animation);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (natureMediaPlayer != null) {
            natureMediaPlayer.release();
            natureMediaPlayer = null;
        }
        if (ambientMediaPlayer != null) {
            ambientMediaPlayer.release();
            ambientMediaPlayer = null;
        }
    }
}
