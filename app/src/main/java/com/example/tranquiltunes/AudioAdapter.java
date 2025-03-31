package com.example.tranquiltunes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private final Context context;
    private final List<AudioModel> audioList;

    public AudioAdapter(Context context, List<AudioModel> audioList) {
        this.context = context;
        this.audioList = audioList;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        AudioModel audio = audioList.get(position);
        holder.audioTitle.setText(audio.getName());

        Glide.with(context).load(audio.getImageUrl()).placeholder(R.drawable.trees).into(holder.audioImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AudioPlayerActivity.class);
            intent.putExtra("AUDIO_URL", audio.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return audioList.size();
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView audioTitle;
        ImageView audioImage;
        ProgressBar progressBar;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioTitle = itemView.findViewById(R.id.audioTitle);
            audioImage = itemView.findViewById(R.id.audioImage);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
