package com.example.flixster.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;
import com.example.flixster.interfaces.ItemLongClickListener;

public class MoviePopularViewHolder extends RecyclerView.ViewHolder {
    ImageView ivPoster;
    RelativeLayout layout;

    public MoviePopularViewHolder(@NonNull View itemView, ItemClickListener itemClickListener, ItemLongClickListener itemLongClickListener) {
        super(itemView);
        ivPoster  = itemView.findViewById(R.id.ivPoster);
        layout = itemView.findViewById(R.id.layout);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClicked(getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemLongClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            }
        });

    }

    public ImageView getIvPoster() {
        return ivPoster;
    }

    public RelativeLayout getLayout() {
        return layout;
    }

}
