package com.example.android.filmmein;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.filmmein.database.AppDatabase;

public class DetailViewModel extends ViewModel {

    private LiveData<Movie> movie;

    public DetailViewModel(AppDatabase database, int movieId) {
        movie = database.movieDao().loadMovieById(movieId);
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }
}
