package com.mobdeve.s15.taboo;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TreasureRVAdapter extends RecyclerView.Adapter<TreasureRVAdapter.MyViewHolder> {
    Context context;
    List<Treasure> treasures;
    private final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.7F); //For button effects, should probably be replaced by something else
    private MediaPlayer buttonSfx;

    public TreasureRVAdapter(Context context, List<Treasure> treasures) {
        this.context = context;
        this.treasures = treasures;
        this.buttonSfx = MediaPlayer.create(context, R.raw.item_sound);
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
        holder.imageView.setImageResource(treasures.get(position).getImageid());

        holder.imageView.setImageResource(treasures.get(position).getImageid());
        holder.itemCount.setText("x" + String.valueOf(treasures.get(position).getCount()));;

        holder.itemCard.setOnClickListener(v -> {
            v.startAnimation(buttonClick);
            buttonSfx.start();
            Intent intent = new Intent(context, TreasureView.class);
            intent.putExtra("ITEM_NAME", treasures.get(index).getName());
            intent.putExtra("ITEM_IMG", treasures.get(index).getImageid());
            intent.putExtra("ITEM_CNT", treasures.get(index).getCount());
            intent.putExtra("ITEM_DESC", treasures.get(index).getLore());
            intent.putExtra("ITEM_RAR", treasures.get(index).getRarity());
            intent.putExtra("ITEM_BON", treasures.get(index).getItemBonus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return treasures.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView itemCount;
        CardView itemCard;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.item_card);
            imageView = itemView.findViewById(R.id.item_thumbnail_iv);
            itemCount = itemView.findViewById(R.id.item_count_tv);
        }
    }
}
