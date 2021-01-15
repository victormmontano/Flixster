package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.BlurTransformation;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;
import com.example.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    ItemClickListener itemClickListener;
    public final int POPULAR = 1;
    public final int REGULAR = 0;

    public MovieAdapter(Context context, List<Movie> movies, ItemClickListener itemClickListener) {
        this.movies = movies;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POPULAR:
                View vp = inflater.inflate(R.layout.item_movie_popular, parent, false);
                viewHolder = new MoviePopularViewHolder(vp, itemClickListener);
                break;
            default:
                View v = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new MovieViewHolder(v, itemClickListener);
                break;
        }
        return viewHolder;



    /*    Log.d("MovieAdapter", "onCreateViewHolder");
       /* View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView, itemClickListener);
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POPULAR:
                View vPopular = inflater.inflate(R.layout.item_movie_popular, parent, false);
                viewHolder = new ViewHolderPopular(vPopular, itemClickListener);
                break;
            default:
                View v = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new ViewHolderPopular(v, itemClickListener);
                break;
        }
        return viewHolder;*/
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*Log.d("MovieAdapter", "onBindViewHolder " + position);
        Movie movie = movies.get(position);
        holder.bind(movie);*/
        switch (holder.getItemViewType()) {
            case POPULAR:
                MoviePopularViewHolder vhp = (MoviePopularViewHolder) holder;
                configurePopularViewHolder(vhp, position);
                break;
            default:
                MovieViewHolder vh = (MovieViewHolder) holder;
                configureDefaultViewHolder(vh, position);
                break;
        }
    }

    private void configureDefaultViewHolder(MovieViewHolder vh, int position) {
        Movie movie = movies.get(position);
        if (movie.isAdult())
            vh.getTvTitle().setTextColor(Color.RED);
        vh.getTvTitle().setText(movie.getTitle());
        vh.getTvOverview().setText(movie.getOverview());
        String imageUrl;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            imageUrl = movie.getBackdropPath();
        else
            imageUrl = movie.getPosterPath();

        Glide.with(context).load(imageUrl).into(vh.getIvPoster());
    }

    private void configurePopularViewHolder(MoviePopularViewHolder vh, int position) {
        Movie movie = movies.get(position);
        Glide.with(context).load(movie.getBackdropPath()).into(vh.getIvPoster());
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (movies.get(position).isPopular())
            return POPULAR;
        else
            return REGULAR;
    }
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
    }*/


