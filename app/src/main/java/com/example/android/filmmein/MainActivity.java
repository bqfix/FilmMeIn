package com.example.android.filmmein;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler {
    /*********************************************
     * The main class that displays a Recycler   *
     * full of Movie Posters to be clicked on    *
     *********************************************/


    //TEST VARIABLES
    private final String TEST_URL_STRING = "https://api.themoviedb.org/3/search/movie?api_key={api-key}&query=Fight";

    //END OF TEST VARIABLES

    //Member Variables
    private RecyclerView mResultsRecycler;
    private MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign member variables
        mResultsRecycler = (RecyclerView) findViewById(R.id.movie_rv);

        //Measure screen size and determine number of columns that will fit
        int numberOfColumns = Utilities.getNumberOfColumns(this);

        //Logic to Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns, LinearLayoutManager.VERTICAL, false);
        mResultsRecycler.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mResultsRecycler.setAdapter(mMovieAdapter);

        //TODO Create custom ItemDecoration to space grid items better

        //Temporary set of movie data for testing
        URL url = Utilities.buildURL(TEST_URL_STRING);

        String jsonResponse = Utilities.makeHttpRequest(url);

        List<Movie> movies = Utilities.parseJSON(this, jsonResponse);

        mMovieAdapter.setNewsStories(movies);


    }

    //Menu creation logic
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case (R.id.action_about) :
                Intent aboutIntent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(aboutIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Item Click Logic, use the movie that was passed from the ViewHolder's onClickListener, and start intent
    @Override
    public void onItemClick(Movie movie) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra(getString(R.string.intent_extra_key), movie);
        startActivity(detailIntent);
    }
}
