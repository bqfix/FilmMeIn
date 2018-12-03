package com.example.android.filmmein;

public class Movie {
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
}
