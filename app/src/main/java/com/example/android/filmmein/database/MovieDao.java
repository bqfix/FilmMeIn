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

    @Query("SELECT * FROM movies ORDER BY title")
    LiveData<List<Movie>> loadAllFavorites();

    @Insert
    void insertTask(MovieEntry movieEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(MovieEntry movieEntry);

    @Delete
    void deleteTask(MovieEntry movieEntry);

    @Query("SELECT * FROM movies WHERE id = :id")
    LiveData<MovieEntry> loadMovieById(int id);
}
