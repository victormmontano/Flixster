package com.example.flixster.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivPoster;

    public MovieViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvOverview = itemView.findViewById(R.id.tvOverview);
        ivPoster  = itemView.findViewById(R.id.ivPoster);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClicked(getAdapterPosition());
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
    /* public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView, ItemClickListener clickListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });


         }

        public void bind(Movie movie) {
            if(movie.isAdult())
                tvTitle.setTextColor(Color.RED);
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageUrl;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                imageUrl = movie.getBackdropPath();
            else
                imageUrl = movie.getPosterPath();

            Glide.with(context).load(imageUrl).into(ivPoster);

        }
    }*/

}
