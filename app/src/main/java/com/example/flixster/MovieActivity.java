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

import okhttp3.Headers;

public class MovieActivity extends AppCompatActivity {
    private static final String DETAILS_URL = "https://api.themoviedb.org/3/movie/%s?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    private static final String TRANSLATION_URL = "https://api.themoviedb.org/3/movie/%s/translations?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private ImageView ivPoster;
    private TextView tvTitle;
    private TextView tvDescription;
    private Spinner spinnerLanguage;
    private RatingBar ratingBar;
    private TextView tvRuntime;

    private AsyncHttpClient client;
    MovieDetailed movie;

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

        client = new AsyncHttpClient();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if(id.isEmpty()){
            Toast.makeText(getApplicationContext(), "Intent passing data did not work", Toast.LENGTH_SHORT).show();
        }
        String url = String.format(DETAILS_URL, id);
        Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    movie = new MovieDetailed(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Glide.with(getApplicationContext()).load(movie.getPosterPath()).into(ivPoster);
                tvTitle.setText(movie.getTitle());
                tvDescription.setText(movie.getOverview());
                ratingBar.setRating((float) movie.getRating());
                tvRuntime.setText(movie.getFormattedRuntime());

            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "we failed :(", Toast.LENGTH_SHORT).show();
            }
        });

        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(movie!= null) {
                    String newLanguage = spinnerLanguage.getItemAtPosition(i).toString();
                    if (!newLanguage.equals(movie.getLanguage())) {
                        setLanguage(newLanguage);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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