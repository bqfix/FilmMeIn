package com.example.android.filmmein;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    /*********************************************
     * A class to hold helper methods            *
     *********************************************/

    /** A method to dynamically check screen size and determine the number of columns a GridLayoutManager in a RecyclerView should create
     *
     * @param context from the Activity housing the RecyclerView
     * @return the number of columns that will fit the screen
     */
    public static int getNumberOfColumns(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float screenWidth = displayMetrics.widthPixels / displayMetrics.density;
        int posterWidth = (int) (resources.getDimension(R.dimen.poster_width) / displayMetrics.density); //Divided by density to remove the dp from the accessed dimen
        return (int) (screenWidth/posterWidth);
    }


    /** A method to return a List of Movies from the given JSON (likely provided from an http request made to TMdB)
     *
     * @param context from the Activity calling the method, used to access resources
     * @param stringJSON to be parsed
     * @return the list of movies created from the JSON
     */
    public static List<Movie> parseJSON(Context context, String stringJSON){
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(stringJSON);

            JSONArray moviesArray = json.getJSONArray(context.getString(R.string.json_results_key));

            for (int index = 0; index < moviesArray.length(); index++) {
                JSONObject currentMovie = moviesArray.getJSONObject(index);

                String title = currentMovie.getString(context.getString(R.string.title_key));
                String releaseDate = currentMovie.getString(context.getString(R.string.release_date_key));
                String posterImageLink = context.getString(R.string.poster_image_link_base) + currentMovie.getString(context.getString(R.string.poster_image_link_key)); //JSON only returns end of URL, needs a base, hence concatenation
                double voterAverage = currentMovie.getDouble(context.getString(R.string.voter_average_key));
                String plotSynopsis = currentMovie.getString(context.getString(R.string.plot_synopsis_key));

                movies.add(new Movie(title,releaseDate,posterImageLink,voterAverage,plotSynopsis));
            }

        } catch (JSONException exception) {
            Log.e(context.getClass().getSimpleName(), exception.getMessage());
        }


        return movies;
    }


    //TODO Add methods for building url, and making http request
}
