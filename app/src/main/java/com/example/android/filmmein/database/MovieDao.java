package com.example.android.filmmein.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.filmmein.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    /*********************************************
     * Methods that can be called on the DB.     *
     * Should never need update.                 *
     *********************************************/


    @Query("SELECT * FROM favorites ORDER BY mTitle")
    LiveData<List<Movie>> loadAllFavorites();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM favorites WHERE mId = :id")
    LiveData<Movie> loadMovieById(int id);
}
