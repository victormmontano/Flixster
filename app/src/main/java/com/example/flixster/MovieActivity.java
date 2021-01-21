package com.example.flixster;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieActivity extends YouTubeBaseActivity {
    private static final String TRANSLATION_URL = "https://api.themoviedb.org/3/movie/%s/translations?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String RECOMMENDATIONS_URL = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&page=1";
    private  static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private static final String YOUTUBE_API_KEY = "AIzaSyAXy5Ds1KgNNeWTXJpPQPjOlHgjCM_kS5E";


    private ImageView ivPoster;
    private ImageView ivPosterRec1;
    private ImageView ivPosterRec2;
    private ImageView ivPosterRec3;
    private TextView tvTitle;
    private TextView tvDescription;
    private Spinner spinnerLanguage;
    private RatingBar ratingBar;
    private TextView tvReleaseDate;
    private YouTubePlayerView playerView;

    private AsyncHttpClient client;
    Movie movie;
    List<Movie> recommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ivPoster = findViewById(R.id.ivMoviePoster);
        tvTitle = findViewById(R.id.tvMovieTitle);
        tvDescription = findViewById(R.id.tvMovieDescription);
        spinnerLanguage = findViewById(R.id.spinnerLanguage);
        ratingBar = findViewById(R.id.ratingBar);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        ivPosterRec1 = findViewById(R.id.ivRec1);
        ivPosterRec2 = findViewById(R.id.ivRec2);
        ivPosterRec3 = findViewById(R.id.ivRec3);
        playerView = (YouTubePlayerView) findViewById(R.id.ivPoster);


        client = new AsyncHttpClient();


        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        if(movie == null){
            Toast.makeText(getApplicationContext(), "Id not passed", Toast.LENGTH_SHORT).show();
        }
        else{
            //Glide.with(getApplicationContext()).load(movie.getBackdropPath()).into(ivPoster);
            tvTitle.setText(movie.getTitle());
            tvDescription.setText(movie.getOverview());
            ratingBar.setRating((float) movie.getRating());
            tvReleaseDate.setText(movie.getReleaseDate());
            setRecommendations(movie.getStringId());
            setSpinnerClicker();
        }

        client.get(String.format(VIDEOS_URL, movie.getId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    boolean found = false;
                    int j = 0;
                    while(!found && j < results.length()){
                        JSONObject object = results.getJSONObject(j);
                        String site = object.getString("site");
                        if(site.equals("YouTube"))
                            found = true;
                        j++;
                    }

                    if(found){
                        String youtubeKey = results.getJSONObject(j-1).getString("key");
                        initializeYoutube(youtubeKey);
                        Log.d("MovieActivity", youtubeKey);
                    }
                    else {
                        //TODO: https://pierfrancesco-soffritti.medium.com/customize-android-youtube-players-ui-9f32da9e8505
                        Glide.with(getApplicationContext()).load(movie.getBackdropPath()).into(ivPoster);
                    }


                } catch (JSONException e) {
                    Log.e("MovieActivity", "Failed to parse JSON array", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
            }
        });

    }

    private void initializeYoutube(String youtubeKey) {
        playerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("MovieActivity", "onSuccess");
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("MovieActivity", "onInitializeFailure");
            }
        });
    }

    private void setSpinnerClicker() {
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newLanguage = spinnerLanguage.getItemAtPosition(i).toString();
                if (!newLanguage.equals(movie.getLanguage())) {
                    setLanguage(newLanguage);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setRecommendations(String stringId) {
        recommendations = new ArrayList<>();
        //String url = "https://api.themoviedb.org/3/movie/%s/similar?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&page=1";
        String url = String.format(RECOMMENDATIONS_URL, stringId);
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    //System.out.println(jsonArray.toString());
                    //Toast.makeText(getApplicationContext(), jsonArray.length(), Toast.LENGTH_SHORT).show();
                    for(int j = 0 ;j < 3 && j < jsonArray.length(); j++){
                        recommendations.add(new Movie(jsonArray.getJSONObject(j)));
                    }
                    Glide.with(getApplicationContext()).load(recommendations.get(0).getPosterPath()).into(ivPosterRec1);
                    Glide.with(getApplicationContext()).load(recommendations.get(1).getPosterPath()).into(ivPosterRec2);
                    Glide.with(getApplicationContext()).load(recommendations.get(2).getPosterPath()).into(ivPosterRec3);
                    setPosterClickListeners();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });


    }

    private void setPosterClickListeners() {

        ivPosterRec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /*Intent intent = new Intent();
               intent.putExtra("id", recommendations.get(0).getStringId());
               startActivity(intent);*/
                /*getIntent().putExtra("id", recommendations.get(0).getStringId());
                recreate();*/
                finish();
                startActivity(getIntent().putExtra("movie", Parcels.wrap(recommendations.get(0))));
                overridePendingTransition(0, 0);
            }
        });

        ivPosterRec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent().putExtra("movie", Parcels.wrap(recommendations.get(1))));
                overridePendingTransition(0, 0);
            }
        });

        ivPosterRec3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent().putExtra("movie", Parcels.wrap(recommendations.get(2))));
                overridePendingTransition(0, 0);
            }
        });

    }

    private void setLanguage(String newLanguage){
        movie.setLanguage(newLanguage);
        String url = String.format(TRANSLATION_URL, movie.getStringId());
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray translationsArray = jsonObject.getJSONArray("translations");
                    int j = 0;
                    boolean found = false;
                    while(!found && j < translationsArray.length()) {
                        JSONObject translation = translationsArray.getJSONObject(j);
                        String name = translation.getString("name");
                        if (name.equals(newLanguage)) {
                            JSONObject data = translation.getJSONObject("data");
                            String title = data.getString("title");
                            String overview = data.getString("overview");
                            if (title.isEmpty()) {
                                title = movie.getDefaultTitle();
                            }
                            if (overview.isEmpty()) {
                                overview = movie.getDefaultOverview();
                            }
                            if(title.isEmpty() && overview.isEmpty())
                                Toast.makeText(getApplicationContext(), "Translation unavailable for this language", Toast.LENGTH_SHORT).show();


                            movie.setOverView(overview);
                            movie.setTitle(title);
                            tvTitle.setText(title);
                            tvDescription.setText(overview);
                            found = true;
                        }
                        j++;
                    }
                    if(!found){
                        Toast.makeText(getApplicationContext(), "translation unavailable for this language", Toast.LENGTH_SHORT).show();
                    }

                   /* for(int j = 0; j < translationsArray.length(); j++){
                        JSONObject translation = translationsArray.getJSONObject(j);
                        String name = translation.getString("name");
                        if(name.equals(newLanguage)){
                            JSONObject data = translation.getJSONObject("data");
                            String title = data.getString("title");
                            String overview = data.getString("overview");
                            if(title.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Title unavailable for this language", Toast.LENGTH_SHORT).show();
                                title = movie.getDefaultTitle();
                            }
                            if(overview.isEmpty()){
                                Toast.makeText(getApplicationContext(), "Overview unavailable for this language", Toast.LENGTH_SHORT).show();
                                overview = movie.getDefaultOverview();
                            }

                            movie.setOverView(overview);
                            movie.setTitle(title);
                            tvTitle.setText(title);
                            tvDescription.setText(overview);
                            j = translationsArray.length();
                        }*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "fix translation api call", Toast.LENGTH_SHORT).show();

            }
        });

    }
}