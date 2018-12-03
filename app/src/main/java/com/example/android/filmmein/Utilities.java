package com.example.android.filmmein;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

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
        int posterWidth = (int) (resources.getDimension(R.dimen.poster_width) / displayMetrics.density);
        return (int) (screenWidth/posterWidth);
    }

    //TODO Add methods for building url, making http request, and parsing JSON
}
