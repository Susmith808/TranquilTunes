package com.example.tranquiltunes;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class SessionTimerDialog extends DialogFragment {

    public interface TimerListener {
        void onTimeSelected(int minutes);
    }

    private TimerListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TimerListener) {
            listener = (TimerListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TimerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create the dialog
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Set the content view for the dialog
        dialog.setContentView(R.layout.dialog_number_picker);

        // Initialize views
        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        // Set NumberPicker values (e.g., 1 to 60 minutes)
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(10); // Default value 10 minutes

        // Set the confirm button click listener
        confirmButton.setOnClickListener(view -> {
            // Get the selected time from the NumberPicker
            int selectedTime = numberPicker.getValue();

            // Notify the listener
            listener.onTimeSelected(selectedTime);

            // Dismiss the dialog
            dismiss();
        });

        return dialog;
    }
}
