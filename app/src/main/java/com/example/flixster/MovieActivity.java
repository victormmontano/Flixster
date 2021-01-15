package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.flixster.models.MovieDetailed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MovieActivity extends AppCompatActivity {
    private static final String DETAILS_URL = "https://api.themoviedb.org/3/movie/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    private static final String TRANSLATION_URL = "https://api.themoviedb.org/3/movie/%s/translations?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String RECOMMENDATIONS_URL = "https://api.themoviedb.org/3/movie/%s/recommendations?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&page=1";
    private ImageView ivPoster;
    private ImageView ivPosterRec1;
    private ImageView ivPosterRec2;
    private ImageView ivPosterRec3;
    private TextView tvTitle;
    private TextView tvDescription;
    private Spinner spinnerLanguage;
    private RatingBar ratingBar;
    private TextView tvRuntime;
    private TextView tvReleaseDate;

    private AsyncHttpClient client;
    MovieDetailed movie;
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
        tvRuntime = findViewById(R.id.tvRuntime);
        tvReleaseDate = findViewById(R.id.tvReleaseDate);
        ivPosterRec1 = findViewById(R.id.ivRec1);
        ivPosterRec2 = findViewById(R.id.ivRec2);
        ivPosterRec3 = findViewById(R.id.ivRec3);

        client = new AsyncHttpClient();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id.isEmpty()){
            Toast.makeText(getApplicationContext(), "Id not passed", Toast.LENGTH_SHORT).show();
        }
        String url = String.format(DETAILS_URL, id);
      //  Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    movie = new MovieDetailed(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Glide.with(getApplicationContext()).load(movie.getBackdropPath()).into(ivPoster);
                tvTitle.setText(movie.getTitle());
                tvDescription.setText(movie.getOverview());
                ratingBar.setRating((float) movie.getRating());
                tvRuntime.setText(movie.getFormattedRuntime());
                tvReleaseDate.setText(movie.getReleaseDate());
                setRecommendations(movie.getStringId());
                setSpinnerClicker();
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "we failed :(", Toast.LENGTH_SHORT).show();
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
                startActivity(getIntent().putExtra("id", recommendations.get(0).getStringId()));
                overridePendingTransition(0, 0);
            }
        });

        ivPosterRec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent().putExtra("id", recommendations.get(1).getStringId()));
                overridePendingTransition(0, 0);
            }
        });

        ivPosterRec3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent().putExtra("id", recommendations.get(2).getStringId()));
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
                                Toast.makeText(getApplicationContext(), "Title unavailable for this language", Toast.LENGTH_SHORT).show();
                                title = movie.getDefaultTitle();
                            }
                            if (overview.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Overview unavailable for this language", Toast.LENGTH_SHORT).show();
                                overview = movie.getDefaultOverview();
                            }

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