package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

        // Set text for instrument name and description
        holder.instrumentName.setText(instrument.getInstrumentname());
        holder.instrumentDescription.setText(instrument.getInstrumentdescription());

        // Load image using Glide if URL is available
        Glide.with(context)
                .load(instrument.getImageURL()) // Assuming getImageURL() method exists in InstrumentFunc
                .centerCrop()
                .into(holder.instrumentBackgroundImage);

        // Set up button click listener
        holder.fetchInstrumentNameButton.setOnClickListener(v -> {
            String selectedInstrument = instrument.getInstrumentname();
            GlobalData.getInstance().setSelectedInstrument(selectedInstrument);

            // Pass the selected instrument to MainActivity
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("selectedInstrument", selectedInstrument);
            context.startActivity(intent);

            // Display confirmation message
            Toast.makeText(context, "Selected Instrument: " + selectedInstrument, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return instrumentList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView instrumentName, instrumentDescription;
        Button fetchInstrumentNameButton;
        ImageView instrumentBackgroundImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize views
            instrumentName = itemView.findViewById(R.id.instrumentnameid);
            instrumentDescription = itemView.findViewById(R.id.instrumentdescid);
            fetchInstrumentNameButton = itemView.findViewById(R.id.chooseinstrumentbtnid);
            instrumentBackgroundImage = itemView.findViewById(R.id.instrumentBackgroundImageid); // Initialize ImageView
        }
    }
}
