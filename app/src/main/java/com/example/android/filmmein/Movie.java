package com.example.android.filmmein;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Parcelable {
    /*********************************************
     * A class to hold a movie object,           *
     * parsed from JSON from the theMovieDB API  *
     *********************************************/

    //Member Variables
    private String mTitle;
    private String mReleaseDate;
    private String mPosterImageLink;
    private double mVoterAverage;
    private String mPlotSynopsis;

    private final String LOG_TAG = getClass().getSimpleName();

    //Constructor
    public Movie(String title, String releaseDate, String posterImageLink, double voterAverage, String plotSynopsis) {
        mTitle = title;
        mPosterImageLink = posterImageLink;
        mVoterAverage = voterAverage;
        mPlotSynopsis = plotSynopsis;

        //Automatically format date
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

            Date inputDate = inputFormat.parse(releaseDate);
            mReleaseDate = outputFormat.format(inputDate);
        } catch (ParseException exception) {
            mReleaseDate = null;
            Log.e(LOG_TAG, exception.getMessage());
        }
    }

    //Getter methods
    public String getTitle() {
        return mTitle;
    }
    public String getReleaseDate() {
        return mReleaseDate;
    }
    public String getPosterImageLink() {
        return mPosterImageLink;
    }
    public double getVoterAverage() {
        return mVoterAverage;
    }
    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }


    //Parcelable logic
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mPosterImageLink);
        dest.writeDouble(this.mVoterAverage);
        dest.writeString(this.mPlotSynopsis);
    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mReleaseDate = in.readString();
        this.mPosterImageLink = in.readString();
        this.mVoterAverage = in.readDouble();
        this.mPlotSynopsis = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
