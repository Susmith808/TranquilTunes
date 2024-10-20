package com.example.tranquiltunes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Padadapter extends RecyclerView.Adapter<Padadapter.MyViewHolder>{

    Context padcontext;

    ArrayList<PadFunc> padlist;

    public Padadapter(Context padcontext, ArrayList<PadFunc> padlist) {
        this.padcontext = padcontext;
        this.padlist = padlist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(padcontext).inflate(R.layout.paditems,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        PadFunc padfunc=padlist.get(position);
        holder.padname.setText(padfunc.getPadname());
        holder.paddescription.setText(padfunc.getPaddescription());
    }

    @Override
    public int getItemCount() {
        return padlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView padname,paddescription ;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            padname=itemView.findViewById(R.id.padnameid);
            paddescription=itemView.findViewById(R.id.paddescid);
        }
    }
}