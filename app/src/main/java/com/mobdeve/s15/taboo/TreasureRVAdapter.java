package com.mobdeve.s15.taboo;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TreasureRVAdapter extends RecyclerView.Adapter<TreasureRVAdapter.MyViewHolder> {
    Context context;
    List<Treasure> treasures;

    public TreasureRVAdapter(Context context, List<Treasure> treasures) {
        this.context = context;
        this.treasures = treasures;
    }

    @NonNull
    @Override
    public TreasureRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.collection_view_row, parent, false);
        return new TreasureRVAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreasureRVAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(treasures.get(position).getImageid());
        holder.tvName.setText(treasures.get(position).getName());
        holder.tvBonus.setText(treasures.get(position).getItemBonus());


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, TreasureView.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return treasures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvBonus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_thumbnail_iv);
            tvName = itemView.findViewById(R.id.item_name_tv);
            tvBonus = itemView.findViewById(R.id.item_bonus_tv);
        }
    }
}
