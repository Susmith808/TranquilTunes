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
        Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.dialog_number_picker);

        NumberPicker numberPicker = dialog.findViewById(R.id.numberPicker);
        Button confirmButton = dialog.findViewById(R.id.confirmButton);

        // Set NumberPicker values (e.g., 1 to 60 minutes)
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(10); // Default 10 minutes

        // Confirm selection
        confirmButton.setOnClickListener(view -> {
            int selectedTime = numberPicker.getValue();
            listener.onTimeSelected(selectedTime);
            dismiss();
        });

        return dialog;
    }
}
