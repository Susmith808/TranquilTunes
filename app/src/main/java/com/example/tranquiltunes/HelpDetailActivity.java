package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class HelpDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        Button openGeneratorButton = findViewById(R.id.openGeneratorButton);

        String topic = getIntent().getStringExtra("topic");
        titleTextView.setText(topic);

        String description = getDescription(topic);
        descriptionTextView.setText(description);

        openGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGeneratorPage(topic);
            }
        });
    }

    private String getDescription(String topic) {
        switch (topic) {
            case "Ambient Music":
                return "Ambient Music Generator\n" +
                        "\n" +
                        "Ambient music is a genre of soundscapes designed to create a soothing background atmosphere. The BrainTones Ambient Music Generator crafts unique soundscapes based on user-selected emotions and instruments. This feature employs randomized musical note generation to create evolving, immersive auditory environments that promote relaxation and creativity.\n" +
                        "\n" +
                        "How It Works:\n" +
                        "\n" +
                        "Uses pre-recorded musical notes categorized by emotion.\n" +
                        "\n" +
                        "Selects and plays notes in a randomized yet harmonious sequence.\n" +
                        "\n" +
                        "Adjusts playback dynamically to maintain a fluid, evolving atmosphere.";
            case "Solfeggio Frequencies":
                return "Solfeggio Frequencies\n" +
                        "\n" +
                        "Solfeggio frequencies are a set of ancient tones believed to have healing properties. These frequencies correspond to specific vibrations that can positively influence the mind and body.\n" +
                        "\n" +
                        "Key Frequencies & Their Benefits:\n" +
                        "\n" +
                        "174 Hz – Reduces pain and tension.\n" +
                        "\n" +
                        "285 Hz – Supports healing and cell regeneration.\n" +
                        "\n" +
                        "396 Hz – Helps overcome fear and guilt.\n" +
                        "\n" +
                        "417 Hz – Facilitates change and removes negative energy.\n" +
                        "\n" +
                        "528 Hz – Known as the \"Love Frequency,\" enhances DNA repair.\n" +
                        "\n" +
                        "639 Hz – Improves relationships and communication.\n" +
                        "\n" +
                        "741 Hz – Detoxifies the body and mind.\n" +
                        "\n" +
                        "852 Hz – Enhances intuition and spiritual awareness.\n" +
                        "\n" +
                        "963 Hz – Connects to higher consciousness.";
            case "Isochronic Tones":
                return "Isochronic Tones\n" +
                        "\n" +
                        "Isochronic tones are single tones that pulse on and off at a specific frequency. Unlike binaural beats, they do not require headphones and can be more effective for some individuals in brainwave entrainment.\n" +
                        "\n" +
                        "Benefits of Isochronic Tones:\n" +
                        "\n" +
                        "Improve focus and concentration.\n" +
                        "\n" +
                        "Aid in relaxation and sleep.\n" +
                        "\n" +
                        "Help with meditation and mindfulness.\n" +
                        "\n" +
                        "Enhance cognitive performance.\n" +
                        "\n" +
                        "How to Use:\n" +
                        "\n" +
                        "Choose a tone based on your mental state or desired effect.\n" +
                        "\n" +
                        "Adjust the pulse speed to match brainwave frequencies (e.g., slow for relaxation, fast for focus).\n" +
                        "\n" +
                        "Listen in a quiet space for best results.";
            case "Noise Generator":
                return "Noise Generator\n" +
                        "\n" +
                        "Noise is often used in sound therapy to mask unwanted sounds, improve focus, and promote relaxation. BrainTones includes various types of noise to suit different needs:\n" +
                        "\n" +
                        "White Noise: Evenly distributed frequencies, useful for concentration and sleep.\n" +
                        "\n" +
                        "Pink Noise: Emphasizes lower frequencies, beneficial for deep sleep.\n" +
                        "\n" +
                        "Brown Noise: Stronger low frequencies, effective for relaxation and anxiety reduction.\n" +
                        "\n" +
                        "Blue Noise: Higher frequencies, useful for enhancing alertness.\n" +
                        "\n" +
                        "Violet Noise: Boosts high frequencies, sometimes used for tinnitus relief.";
            case "Binaural Beats":
                return "Binaural Beats\n" +
                        "\n" +
                        "Binaural beats are a form of auditory illusion that occurs when two slightly different frequencies are played in each ear. The brain perceives the difference between these frequencies and synchronizes to the resulting beat, influencing mental states.\n" +
                        "\n" +
                        "Frequency Ranges & Effects:\n" +
                        "\n" +
                        "Delta (0.5 - 4 Hz): Deep sleep and relaxation.\n" +
                        "\n" +
                        "Theta (4 - 8 Hz): Meditation, creativity, and intuition.\n" +
                        "\n" +
                        "Alpha (8 - 14 Hz): Relaxed awareness and mental clarity.\n" +
                        "\n" +
                        "Beta (14 - 30 Hz): Active thinking and problem-solving.\n" +
                        "\n" +
                        "Gamma (30 - 100 Hz): High-level cognition and consciousness.\n" +
                        "\n" +
                        "How to Use:\n" +
                        "\n" +
                        "Requires headphones for optimal effect.\n" +
                        "\n" +
                        "Select a binaural beat based on your goal (focus, relaxation, sleep, etc.).\n" +
                        "\n" +
                        "Adjust volume and duration to personal preference.";
            default:
                return "Information not available.";
        }
    }

    private void openGeneratorPage(String topic) {
        Intent intent;
        switch (topic) {
            case "Ambient Music":
                intent = new Intent(this, Emotion.class);
                break;
            case "Solfeggio Frequencies":
                intent = new Intent(this, SolfeggioListActivity.class);
                break;
            case "Isochronic Tones":
                intent = new Intent(this, IsochronicToneListActivity.class);
                break;
            case "Noise Generator":
                intent = new Intent(this, NoiseGeneratorActivity.class);
                break;
            case "Binaural Beats":
                intent = new Intent(this, BinauralBeatsActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }
}
