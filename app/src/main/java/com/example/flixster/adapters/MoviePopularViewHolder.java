package com.example.flixster.adapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;

public class MoviePopularViewHolder extends RecyclerView.ViewHolder {
    ImageView ivPoster;

    public MoviePopularViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        ivPoster  = itemView.findViewById(R.id.ivMoviePoster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClicked(getAdapterPosition());
            }
        });

    }


    public ImageView getIvPoster() {
        return ivPoster;
    }


    /*
    public class ViewHolderPopular extends RecyclerView.ViewHolder {
        ImageView ivPoster;

        public ViewHolderPopular(@NonNull View itemView, ItemClickListener clickListener) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });


        }

        public void bind(Movie movie) {

            Glide.with(context).load(movie.getBackdropPath()).into(ivPoster);

        }
    }
     */
}
