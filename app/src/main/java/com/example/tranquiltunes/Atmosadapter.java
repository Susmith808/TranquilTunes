package com.example.tranquiltunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Atmosadapter extends RecyclerView.Adapter<Atmosadapter.MyViewHolder>{

    Context context;

    ArrayList<Func> list;

    public Atmosadapter(Context context, ArrayList<Func> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.soundscapeitems,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    Func func=list.get(position);
    holder.atmosname.setText(func.getAtmosname());
    holder.atmosdescription.setText(func.getAtmosdescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView atmosname, atmosdescription;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            atmosname=itemView.findViewById(R.id.atmosnameid);
            atmosdescription=itemView.findViewById(R.id.atmosdescid);
        }
    }
}
