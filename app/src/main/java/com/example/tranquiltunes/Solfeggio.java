package com.example.tranquiltunes;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solfeggio extends AppCompatActivity {

    private int selectedFrequency;
    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private Thread audioThread;
    private int sessionDuration = 5;
    private float volume = 1.0f;

    private TextView stateText, beatFreqText, sessionDurationText;
    private ImageView freq,setDurationButton, playButton, selectSoundButton, playAmbientButton, natureToggleButton, ambientToggleButton;
    private SeekBar beatSeekBar, natureVolumeSeekBar, ambientVolumeSeekBar;

    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private MediaPlayer natureMediaPlayer, ambientMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solfeggio);

        String frequencyString = getIntent().getStringExtra("frequency");
        selectedFrequency = Integer.parseInt(frequencyString.split(" ")[0]);
        freq=findViewById(R.id.freq);
        stateText = findViewById(R.id.stateText);
        beatFreqText = findViewById(R.id.beatFreqText);
        sessionDurationText = findViewById(R.id.sessionDurationText);
        playButton = findViewById(R.id.playButton);
        setDurationButton = findViewById(R.id.setDurationButton);
        beatSeekBar = findViewById(R.id.beatSeekBar);

        selectSoundButton = findViewById(R.id.selectSoundButton);
        playAmbientButton = findViewById(R.id.playAmbientButton);
        natureToggleButton = findViewById(R.id.NatureToggleBtn);
        ambientToggleButton = findViewById(R.id.AmbientToggleBtn);

        // Volume Control Sliders
        natureVolumeSeekBar = findViewById(R.id.natureVolumeSeekBar);
        ambientVolumeSeekBar = findViewById(R.id.ambientVolumeSeekBar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading sounds...");
        progressDialog.setCancelable(false);

        stateText.setText("State: Relaxed");
        beatFreqText.setText("Beat Frequency: 10 Hz");
        sessionDurationText.setText("Session Duration: " + sessionDuration + " minutes");

        beatSeekBar.setMax(30);
        beatSeekBar.setProgress(10);
        beatSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                beatFreqText.setText("Beat Frequency: " + progress + " Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setDurationButton.setOnClickListener(v -> showDurationPicker());

        playButton.setOnClickListener(v -> {
            if (!isPlaying) {
                playFrequency(selectedFrequency);
                playButton.setImageResource(R.drawable.pause100); // Change to stop icon
            } else {
                stopFrequency();
                playButton.setImageResource(R.drawable.play100); // Change to play icon
            }
        });

        selectSoundButton.setOnClickListener(v -> fetchSoundsFromFirebase("natureSounds", true));
        playAmbientButton.setOnClickListener(v -> fetchSoundsFromFirebase("ambientMusic", false));

        natureToggleButton.setOnClickListener(v -> handleToggle(natureToggleButton, true));
        ambientToggleButton.setOnClickListener(v -> handleToggle(ambientToggleButton, false));

        // Volume Controls for Nature and Ambient
        natureVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (natureMediaPlayer != null) {
                    float volume = progress / 100f;
                    natureMediaPlayer.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ambientVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ambientMediaPlayer != null) {
                    float volume = progress / 100f;
                    ambientMediaPlayer.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        natureToggleButton.setVisibility(View.GONE);
        ambientToggleButton.setVisibility(View.GONE);
        natureVolumeSeekBar.setVisibility(View.GONE);
        ambientVolumeSeekBar.setVisibility(View.GONE);
    }

    private void handleToggle(ImageView button, boolean isNatureSound) {
        MediaPlayer player = isNatureSound ? natureMediaPlayer : ambientMediaPlayer;

        if (player == null) {
            Toast.makeText(this, "Please select a sound first.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!player.isPlaying()) {
            player.start();
            button.setImageResource(R.drawable.pause50); // Change to pause icon
        } else {
            player.pause();
            button.setImageResource(R.drawable.play50); // Change to play icon
        }
    }


    private void stopFrequency() {
        isPlaying = false;

        if (audioThread != null) {
            try {
                audioThread.join(); // Ensure the audio generation thread is stopped
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audioThread = null;
        }

        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
            audioTrack = null;
        }

        stateText.setText("State: Frequency Stopped");
    }

    private void playFrequency(int frequency) {
        isPlaying = true;
        int sampleRate = 44100;
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                bufferSize, AudioTrack.MODE_STREAM);

        audioTrack.setVolume(volume);
        audioTrack.play();

        // Create and start the audio generation thread
        audioThread = new Thread(() -> {
            short[] buffer = new short[bufferSize];
            double phase = 0;
            double increment = 2 * Math.PI * frequency / sampleRate;

            while (isPlaying) {
                for (int i = 0; i < bufferSize; i++) {
                    buffer[i] = (short) (Math.sin(phase) * 32767);
                    phase += increment;
                    if (phase > 2 * Math.PI) phase -= 2 * Math.PI;
                }
                audioTrack.write(buffer, 0, bufferSize);
            }
        });

        audioThread.start();
        stateText.setText("State: Playing Frequency - " + frequency + " Hz");
    }

    private void showDurationPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_number_picker, null);
        builder.setView(dialogView);

        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        ImageView confirmButton = dialogView.findViewById(R.id.confirmButton);

        // Configure NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(30);
        numberPicker.setValue(sessionDuration);

        AlertDialog dialog = builder.create();

        confirmButton.setOnClickListener(v -> {
            sessionDuration = numberPicker.getValue();
            sessionDurationText.setText("Session Duration: " + sessionDuration + " minutes");
            dialog.dismiss();
        });

        dialog.show();
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
                if (soundList.isEmpty()) {
                    Toast.makeText(Solfeggio.this, "No sounds available in this category.", Toast.LENGTH_SHORT).show();
                } else {
                    showSoundPickerDialog(soundList, soundUrls, isNatureSound);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressDialog.dismiss();
                Log.e("Firebase", "Error fetching sounds: ", error.toException());
            }
        });
    }

    private void showSoundPickerDialog(List<String> soundList, List<String> soundUrls, boolean isNatureSound) {
        new AlertDialog.Builder(this)
                .setTitle("Select a Sound")
                .setItems(soundList.toArray(new String[0]), (dialog, which) -> {
                    String selectedSoundUrl = soundUrls.get(which);
                    playSelectedAudio(selectedSoundUrl, isNatureSound);

                    if (isNatureSound) {
                        natureToggleButton.setVisibility(View.VISIBLE);
                        natureVolumeSeekBar.setVisibility(View.VISIBLE);
                    } else {
                        ambientToggleButton.setVisibility(View.VISIBLE);
                        ambientVolumeSeekBar.setVisibility(View.VISIBLE);
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
                Toast.makeText(Solfeggio.this, "Sound is ready. Press the toggle button to play.", Toast.LENGTH_SHORT).show();
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(Solfeggio.this, "Failed to play sound.", Toast.LENGTH_SHORT).show();
        }

        if (isNatureSound) {
            natureMediaPlayer = player;
        } else {
            ambientMediaPlayer = player;
        }
    }
}
