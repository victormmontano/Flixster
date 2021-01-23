package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import com.example.flixster.MainActivity;
import com.example.flixster.MovieActivity;
import com.example.flixster.R;
import com.example.flixster.interfaces.ItemClickListener;
import com.example.flixster.interfaces.ItemLongClickListener;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Movie> movies;
    ItemLongClickListener itemLongClickListener;
    public final int POPULAR = 1;
    public final int REGULAR = 0;

    public MovieAdapter(Context context, List<Movie> movies, ItemLongClickListener itemLongClickListener) {
        this.movies = movies;
        this.context = context;
        this.itemLongClickListener = itemLongClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case POPULAR:
                View vp = inflater.inflate(R.layout.item_movie_popular, parent, false);
                viewHolder = new MoviePopularViewHolder(vp, itemLongClickListener);
                break;
            default:
                View v = inflater.inflate(R.layout.item_movie, parent, false);
                viewHolder = new MovieViewHolder(v, itemLongClickListener);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder " + position);
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
        if(movie.getVoteAverage() <= 5.0)
            vh.getLayout().setBackgroundColor(Color.parseColor("#6B818C"));
        else
            vh.getLayout().setBackgroundColor(Color.parseColor("#35605A"));
        Glide.with(context).load(imageUrl).transform(new RoundedCornersTransformation(50, 10)).into(vh.getIvPoster());
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieActivity.class);
                Movie m = movies.get(position);
                intent.putExtra("movie", Parcels.wrap(m));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, vh.getIvPoster(), "movie");
                context.startActivity(intent, options.toBundle());
            }
        });


    }

    private void configurePopularViewHolder(MoviePopularViewHolder vh, int position) {
        Movie movie = movies.get(position);
        if(movie.getVoteAverage() <= 5.0)
            vh.getLayout().setBackgroundColor(Color.parseColor("#6B818C"));
        else
            vh.getLayout().setBackgroundColor(Color.parseColor("#35605A"));
        Glide.with(context).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(100, 10)).into(vh.getIvPoster());
        vh.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieActivity.class);
                Movie m = movies.get(position);
                intent.putExtra("movie", Parcels.wrap(m));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, vh.getIvPoster(), "movie");
                context.startActivity(intent, options.toBundle());
            }
        });
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



