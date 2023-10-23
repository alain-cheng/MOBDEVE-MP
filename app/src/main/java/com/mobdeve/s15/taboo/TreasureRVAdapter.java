package com.mobdeve.s15.taboo;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TreasureRVAdapter extends RecyclerView.Adapter<TreasureRVAdapter.MyViewHolder> {
    Context context;
    List<Treasure> treasures;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else

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
        int index = holder.getAdapterPosition();
        String color = "";

        holder.imageView.setImageResource(treasures.get(position).getImageid());
        holder.tvName.setText(treasures.get(position).getName());
        holder.tvBonus.setText(treasures.get(position).getItemBonus());
        holder.itemCount.setText("x" + String.valueOf(treasures.get(position).getCount()));

        switch (treasures.get(position).getRarity()){
            case "COMMON":{
                color = "#A0A0A0";
                break;
            }
            case "RARE":{
                color = "#0094FF";
                break;
            }
            case "FORBIDDEN":{
                color = "#FF006E";
                break;
            }
            case "BLASPHEMY":{
                color = "#B200FF";
            }
        }
        holder.tvName.setTextColor(Color.parseColor(color));

        holder.itemCard.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            Intent intent = new Intent(context, TreasureView.class);
            intent.putExtra("ITEM_NAME", treasures.get(index).getName());
            intent.putExtra("ITEM_IMG", treasures.get(index).getImageid());
            intent.putExtra("ITEM_DESC", treasures.get(index).getLore());
            intent.putExtra("ITEM_RAR", treasures.get(index).getRarity());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return treasures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvName, tvBonus, itemCount;
        CardView itemCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.item_card);
            imageView = itemView.findViewById(R.id.item_thumbnail_iv);
            tvName = itemView.findViewById(R.id.item_name_tv);
            tvBonus = itemView.findViewById(R.id.item_bonus_tv);
            itemCount = itemView.findViewById(R.id.item_count_tv);
        }
    }
}
