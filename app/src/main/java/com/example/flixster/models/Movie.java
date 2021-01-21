package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Movie {
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    String language;
    int id;
    boolean adult;
    boolean popular;
    double voteAverage;
    String defaultTitle;
    String defaultOverview;
    String releaseDate;

    //empty constructor for parcel
    public Movie(){}

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        id = jsonObject.getInt("id");
        adult = jsonObject.getBoolean("adult");
        voteAverage = jsonObject.getDouble("vote_average");
        releaseDate = jsonObject.getString("release_date");

        popular = voteAverage > 5.0;
        language = "English";
        defaultTitle = title;
        defaultOverview = overview;


    }

    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for(int i  = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOverView(String overview){
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath(){
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }

    public int getId(){
        return id;
    }

    public String getStringId(){
        return Integer.toString(id);
    }

    public boolean isAdult(){ return adult; }

    public double getVoteAverage(){ return voteAverage; }

    public boolean isPopular() { return popular;}

    public void togglePopular() {popular = !popular; }



    public String getDefaultTitle(){ return defaultTitle;}
    public String getDefaultOverview(){ return defaultOverview;}


    public String getReleaseDate(){ return releaseDate; }

    public double getRating(){ return voteAverage; }


}
