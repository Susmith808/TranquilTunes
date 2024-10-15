package com.example.tranquiltunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Emoadapter extends RecyclerView.Adapter<Emoadapter.MyViewHolder>{

    Context emocontext;

    ArrayList<EmoFunc> emolist;

    public Emoadapter(Context emocontext, ArrayList<EmoFunc> emolist) {
        this.emocontext = emocontext;
        this.emolist = emolist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(emocontext).inflate(R.layout.emotionitems,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EmoFunc emofunc=emolist.get(position);
        holder.emoname.setText(emofunc.getEmoname());
        holder.emodescription.setText(emofunc.getEmodescription());
    }

    @Override
    public int getItemCount() {
        return emolist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView emoname,emodescription ;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            emoname=itemView.findViewById(R.id.emonameid);
            emodescription=itemView.findViewById(R.id.emodescid);
        }
    }
}