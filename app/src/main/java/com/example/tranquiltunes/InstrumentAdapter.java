package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InstrumentAdapter extends RecyclerView.Adapter<InstrumentAdapter.MyViewHolder> {

    Context context;
    ArrayList<InstrumentFunc> instrumentList;

    public InstrumentAdapter(Context context, ArrayList<InstrumentFunc> instrumentList) {
        this.context = context;
        this.instrumentList = instrumentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.instrumentitems, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InstrumentFunc instrument = instrumentList.get(position);
        holder.instrumentName.setText(instrument.getInstrumentname());
        holder.instrumentDescription.setText(instrument.getInstrumentdescription());

        // Handle button click to set selected instrument
        holder.fetchInstrumentNameButton.setOnClickListener(v -> {
            String selectedInstrument = instrument.getInstrumentname(); // Get the selected instrument
            GlobalData.getInstance().setSelectedInstrument(selectedInstrument); // Set it in GlobalData

            // Optional: show a Toast for confirmation
            Toast.makeText(context, "Selected Instrument: " + selectedInstrument, Toast.LENGTH_SHORT).show();

            // Now you can start the next activity
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return instrumentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView instrumentName, instrumentDescription;
        Button fetchInstrumentNameButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            instrumentName = itemView.findViewById(R.id.instrumentnameid);
            instrumentDescription = itemView.findViewById(R.id.instrumentdescid);
            fetchInstrumentNameButton = itemView.findViewById(R.id.chooseinstrumentbtnid);
        }
    }
}
