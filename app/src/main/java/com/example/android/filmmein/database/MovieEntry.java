//package com.example.android.filmmein.database;
//
//import android.arch.persistence.room.Entity;
//import android.arch.persistence.room.PrimaryKey;
//
//@Entity(tableName = "movies")
//public class MovieEntry {
//
//    /*********************************************
//     * A class representing an entry into the DB *
//     *********************************************/
//
//    //Deprecated?
//
//    @PrimaryKey
//    private int id;
//    private String title;
//    //TODO figure out how to save images offline
//    private String releaseDate;
//    private String posterImageLink;
//    private double voterAverage;
//    private String plotSynopsis;
//
//    //Constructor
//    public MovieEntry(int id, String title, String releaseDate, String posterImageLink, double voterAverage, String plotSynopsis) {
//        this.id = id;
//        this.title = title;
//        this.releaseDate = releaseDate;
//        this.posterImageLink = posterImageLink;
//        this.voterAverage = voterAverage;
//        this.plotSynopsis = plotSynopsis;
//    }
//
//
//    //Getters and setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getReleaseDate() {
//        return releaseDate;
//    }
//
//    public void setReleaseDate(String releaseDate) {
//        this.releaseDate = releaseDate;
//    }
//
//    public String getPosterImageLink() {
//        return posterImageLink;
//    }
//
//    public void setPosterImageLink(String posterImageLink) {
//        this.posterImageLink = posterImageLink;
//    }
//
//    public double getVoterAverage() {
//        return voterAverage;
//    }
//
//    public void setVoterAverage(double voterAverage) {
//        this.voterAverage = voterAverage;
//    }
//
//    public String getPlotSynopsis() {
//        return plotSynopsis;
//    }
//
//    public void setPlotSynopsis(String plotSynopsis) {
//        this.plotSynopsis = plotSynopsis;
//    }
//}
