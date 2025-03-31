package com.example.tranquiltunes;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private LinearLayout questionsContainer;
    private Button submitButton;

    private final List<Question> questions = new ArrayList<>();
    private List<RadioGroup> radioGroups = new ArrayList<>();
    private int[] scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        // Initialize UI Elements
        progressBar = findViewById(R.id.progressBar);
        questionsContainer = findViewById(R.id.questionsContainer);
        submitButton = findViewById(R.id.submitButton);

        initializeQuestions();
        scores = new int[questions.size()];

        populateQuestions();

        submitButton.setOnClickListener(v -> processAnswers());
    }

    private void initializeQuestions() {
        questions.add(new Question("Do you often feel happy and content?",
                new String[]{"Always", "Often", "Sometimes", "Rarely", "Never"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you frequently experience mood swings?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you feel emotionally stable?",
                new String[]{"Always", "Often", "Sometimes", "Rarely", "Never"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you often feel anxious or restless?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you have trouble sleeping due to stress?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("How often do you feel overwhelmed by responsibilities?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you find it easy to relax and unwind?",
                new String[]{"Always", "Often", "Sometimes", "Rarely", "Never"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("How often do you feel socially withdrawn or disconnected?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("Do you feel confident in handling daily stress?",
                new String[]{"Always", "Often", "Sometimes", "Rarely", "Never"},
                new int[]{1, 2, 3, 4, 5}));

        questions.add(new Question("How often do you experience negative thoughts?",
                new String[]{"Never", "Rarely", "Sometimes", "Often", "Always"},
                new int[]{1, 2, 3, 4, 5}));
    }

    private void populateQuestions() {
        radioGroups.clear();
        questionsContainer.removeAllViews();

        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);

            // Add Question Text
            TextView questionText = new TextView(this);
            questionText.setText(q.getQuestionText());
            questionText.setTextSize(18);
            questionText.setPadding(0, 10, 0, 10);
            questionsContainer.addView(questionText);

            // Create Radio Group
            RadioGroup radioGroup = new RadioGroup(this);
            radioGroup.setOrientation(RadioGroup.VERTICAL);

            // Add Options
            for (int j = 0; j < q.getOptions().length; j++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(q.getOptions()[j]);
                radioButton.setTag(q.getScores()[j]);
                radioGroup.addView(radioButton);
            }

            radioGroups.add(radioGroup);
            questionsContainer.addView(radioGroup);
        }
    }

    private void processAnswers() {
        int totalScore = 0;
        int maxPossibleScore = questions.size() * 5;

        for (int i = 0; i < radioGroups.size(); i++) {
            RadioGroup group = radioGroups.get(i);
            int selectedId = group.getCheckedRadioButtonId();

            if (selectedId != -1) {
                RadioButton selectedButton = findViewById(selectedId);
                scores[i] = (int) selectedButton.getTag();
            } else {
                scores[i] = 0; // Default to 0 if unanswered
            }

            totalScore += scores[i];
        }

        double percentage = ((double) totalScore / maxPossibleScore) * 100;
        String psychologicalState = determinePsychologicalState(percentage);

        Intent intent = new Intent(QuestionnaireActivity.this, ResultActivity.class);
        intent.putExtra("STATE", psychologicalState);
        startActivity(intent);
        finish();
    }

    private String determinePsychologicalState(double percentage) {
        if (percentage <= 20) return "RELAXED";
        else if (percentage <= 35) return "MILD_STRESS";
        else if (percentage <= 50) return "MODERATE_STRESS";
        else if (percentage <= 70) return "HIGH_STRESS";
        else return "SEVERE_DISTRESS";
    }
}
