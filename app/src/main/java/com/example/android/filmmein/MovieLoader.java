package com.example.android.filmmein;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.net.URL;
import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {
    /**********************************************
     * A class to create a custom loader to get   *
     * data for the main activity's recycler view *
     **********************************************/

    String mUrlString;

    public MovieLoader(@NonNull Context context, String urlString) {
        super(context);
        this.mUrlString = urlString;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Movie> loadInBackground() {
        if (mUrlString.length() < 1 || mUrlString == null) {
            return null;
        }
        URL url = Utilities.buildURL(mUrlString);
        String jsonResponse = Utilities.makeHttpRequest(url);
        return Utilities.parseJSONForMovieResults(getContext(), jsonResponse);
    }
}
