package com.example.android.filmmein;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    /*********************************************
     * A class to hold a movie object,           *
     * parsed from JSON from the theMovieDB API  *
     *********************************************/

    //Member Variables
    private String mTitle;
    private String mReleaseDate;
    private String mPosterImageLink;
    private double mVoteAverage;
    private String mPlotSynopsis;

    //Constructor
    public Movie(String title, String releaseDate, String posterImageLink, double voteAverage, String plotSynopsis) {
        mTitle = title;
        mReleaseDate = releaseDate; //TODO Use Datetime to reformat
        mPosterImageLink = posterImageLink;
        mVoteAverage = voteAverage;
        mPlotSynopsis = plotSynopsis;
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
    public double getVoteAverage() {
        return mVoteAverage;
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
        dest.writeDouble(this.mVoteAverage);
        dest.writeString(this.mPlotSynopsis);
    }

    protected Movie(Parcel in) {
        this.mTitle = in.readString();
        this.mReleaseDate = in.readString();
        this.mPosterImageLink = in.readString();
        this.mVoteAverage = in.readDouble();
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
