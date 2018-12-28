package com.example.android.filmmein.database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.filmmein.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    /*********************************************
     * Methods that can be called on the DB.     *
     * Should never need update.                 *
     *********************************************/


    @Query("SELECT * FROM movies ORDER BY title")
    LiveData<List<Movie>> loadAllFavorites();

    @Insert
    void insertTask(MovieEntry movieEntry);

    @Delete
    void deleteTask(MovieEntry movieEntry);
}
