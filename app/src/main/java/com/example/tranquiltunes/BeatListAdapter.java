package com.example.tranquiltunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class BeatListAdapter extends BaseAdapter {

    private final Context context;
    private final List<BinauralBeat> beats;

    public BeatListAdapter(Context context, List<BinauralBeat> beats) {
        this.context = context;
        this.beats = beats;
    }

    @Override
    public int getCount() {
        return beats.size();
    }

    @Override
    public Object getItem(int position) {
        return beats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_beat, parent, false);
        }

        TextView beatNameTextView = convertView.findViewById(R.id.beatNameTextView);
        TextView beatDescriptionTextView = convertView.findViewById(R.id.beatDescriptionTextView);

        BinauralBeat beat = beats.get(position);
        beatNameTextView.setText(beat.getName());
        beatDescriptionTextView.setText(beat.getDescription());

        return convertView;
    }
}
