package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailed {
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    String language;
    int id;
    String defaultTitle;
    String defaultOverview;
    String releaseDate;
    int revenue;
    int runtime;
    boolean adult;
    double voteAverage;

    public MovieDetailed(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        id = jsonObject.getInt("id");
        language = "English";
        defaultTitle = title;
        defaultOverview = overview;
        releaseDate = jsonObject.getString("release_date");
        revenue = jsonObject.getInt("revenue");
        runtime = jsonObject.getInt("runtime");
        adult = jsonObject.getBoolean("adult");
        voteAverage = jsonObject.getDouble("vote_average");
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

    /*public void setLanguage(JSONArray jsonArray, String newLanguage) throws JSONException {
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject currObj = jsonArray.getJSONObject(i);
            if( currObj.getString("name")
        }
    }*/

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

    public String getDefaultTitle(){ return defaultTitle;}
    public String getDefaultOverview(){ return defaultOverview;}

    public int getRevenue() { return revenue; }
    public int getRuntime() { return runtime; }
    public String getReleaseDate(){ return releaseDate; }

    public double getVoteAverage(){ return voteAverage; }

    public double getRating(){ return voteAverage/2; }

    public String getFormattedRuntime(){
        String format = "%d hr %d min";
        int s = runtime;
      /*  int hours = 0;
        while(s > 60){
            s -= 60;
            hours++;
        }
        int minutes = s;*/
        return String.format("%02d:%02d", s / 60, (s % 60));
        //return String.format(format, hours, minutes);
    }

}
