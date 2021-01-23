package com.example.flixster.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;
import com.example.flixster.interfaces.ItemLongClickListener;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;
    RelativeLayout layout;

    public MovieViewHolder(@NonNull View itemView, ItemLongClickListener itemLongClickListener) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvOverview = itemView.findViewById(R.id.tvOverview);
        ivPoster  = itemView.findViewById(R.id.ivPoster);
        layout = itemView.findViewById(R.id.layout);


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemLongClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            }
        });

    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvOverview() {
        return tvOverview;
    }

    public ImageView getIvPoster() {
        return ivPoster;
    }

    public RelativeLayout getLayout(){ return layout; }

}
