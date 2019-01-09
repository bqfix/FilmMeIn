package com.example.android.filmmein;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utilities {
    /*********************************************
     * A class to hold helper methods            *
     *********************************************/

    private static final String LOG_TAG = Utilities.class.getSimpleName();

    public static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";

    /** A method to dynamically check screen size and determine the number of columns a GridLayoutManager in a RecyclerView should create
     *
     * @param context from the Activity housing the RecyclerView
     * @return the number of columns that will fit the screen
     */
    public static int getNumberOfColumns(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float density = displayMetrics.density;
        float screenWidth = displayMetrics.widthPixels / density;
        int posterWidth = (int) (resources.getDimension(R.dimen.poster_width) / density); //Divided by density to remove the dp from the accessed dimen
        return (int) (screenWidth/posterWidth);
    }



    /** A method to return a List of Movies from the given JSON (likely provided from an http request made to TMdB)
     *
     * @param context from the Activity calling the method, used to access resources
     * @param stringJSON to be parsed
     * @return the list of movies created from the JSON
     */
    public static List<Movie> parseJSONForMovieResults(Context context, String stringJSON){
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(stringJSON);

            JSONArray moviesArray = json.getJSONArray(context.getString(R.string.json_results_key));

            for (int index = 0; index < moviesArray.length(); index++) {
                JSONObject currentMovie = moviesArray.getJSONObject(index);

                int id = currentMovie.getInt(context.getString(R.string.id_key));
                String title = currentMovie.getString(context.getString(R.string.title_key));
                String releaseDate = currentMovie.getString(context.getString(R.string.release_date_key));
                String posterImageLink = context.getString(R.string.poster_image_link_base) + currentMovie.getString(context.getString(R.string.poster_image_link_key)); //JSON only returns end of URL, needs a base, hence concatenation
                double voterAverage = currentMovie.getDouble(context.getString(R.string.voter_average_key));
                String plotSynopsis = currentMovie.getString(context.getString(R.string.plot_synopsis_key));

                movies.add(new Movie(id, title,releaseDate,posterImageLink,voterAverage,plotSynopsis));
            }

        } catch (JSONException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }


        return movies;
    }

    /**
     * A method to parse JSON and return a movie's trailer link in the form of a Uri
     * @param context from the Activity calling the method, used to access resources
     * @param stringJSON to be parsed
     * @return a Uri to access the trailer
     */
    public static Uri parseMovieForTrailer(Context context, String stringJSON){
        Uri movieUri = Uri.parse(BASE_YOUTUBE_URL);
        try {
            JSONObject json = new JSONObject(stringJSON);

            JSONArray results = json.getJSONArray(context.getString(R.string.video_results_key));

            for (int index = 0; index < results.length(); index++) {
                JSONObject currentVideo = results.getJSONObject(index);
                //Check each video in the list to find the first instance of a trailer
                String videoType = currentVideo.getString(context.getString(R.string.video_type_key));
                String videoSite = currentVideo.getString(context.getString(R.string.video_site_key));

                //If it is, build on the youtube link if youtube is the supported site
                if (videoType.equals(context.getString(R.string.video_type_trailer)) && videoSite.equals(context.getString(R.string.video_site_youtube))) {
                    Uri.Builder builder = movieUri.buildUpon();
                    //Append query, " v = video's key "
                    builder.appendQueryParameter(context.getString(R.string.video_link_query), currentVideo.getString(context.getString(R.string.video_link_key)));
                    movieUri = builder.build();
                    break;
                }
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }

        return movieUri;
    }

    /**
     * A method to parse JSON and return a list of the reviews for the movie (no names, want to keep it anonymous)
     * @param context from the Activity calling the method, used to access resources
     * @param stringJSON to be parsed
     * @return a list of reviews
     */
    public static List<String> parseMovieForReviews(Context context, String stringJSON){
        List<String> reviews = new ArrayList<String>();
        try {
            JSONObject json = new JSONObject(stringJSON);

            JSONArray results = json.getJSONArray(context.getString(R.string.reviews_results_key));

            for (int index = 0; index < results.length(); index++) {
                JSONObject currentReview = results.getJSONObject(index);
                String reviewContent = currentReview.getString(context.getString(R.string.reviews_content_key));
                reviews.add(reviewContent);
            }
        } catch (JSONException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }

        return reviews;
    }

    /**
     * Builds the URL
     *
     * @param stringUrl the url to be input
     * @return The URL to query
     */
    public static URL buildURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, exception.getMessage());
        }
        return url;
    }

    /**
     * Method to make the initial Http request
     *
     * @param url the url to be used (likely generated from buildURL method)
     * @return a string with the full JSON response to be parsed by extractStories method
     */
    public static String makeHttpRequest(URL url) {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read data if valid connection was made
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + responseCode);
            }
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Could not connect");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException exception) {
                Log.e(LOG_TAG, "Failed to close input stream");
            }

        }
        return jsonResponse;
    }


    /**
     * Helper method to perform the reading from the input stream
     *
     * @param inputStream provided from the makeHttpRequest method
     * @return a built JSONResponse to be returned by the makeHttpRequest method
     * @throws IOException that is handled by the makeHttpRequest method which calls this method
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
