package com.example.android.filmmein;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler, LoaderManager.LoaderCallbacks<List<Movie>> {
    /*********************************************
     * The main class that displays a Recycler   *
     * full of Movie Posters to be clicked on    *
     *********************************************/

    //Member Variables
    private RecyclerView mResultsRecycler;
    private MovieAdapter mMovieAdapter;
    private ProgressBar mProgressIndicator;
    private TextView mErrorText;

    private final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key={api-key}&language=en-US&include_adult=false&include_video=false&page=1&sort_by=popularity.desc";

    private final int LOADER_ID = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign member variables
        mResultsRecycler = (RecyclerView) findViewById(R.id.movie_rv);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
        mErrorText = (TextView) findViewById(R.id.error_text);

        //Measure screen size and determine number of columns that will fit
        int numberOfColumns = Utilities.getNumberOfColumns(this);

        //Logic to Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns, LinearLayoutManager.VERTICAL, false);
        mResultsRecycler.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mResultsRecycler.setAdapter(mMovieAdapter);

        //TODO Create custom ItemDecoration to space grid items better

        //Check network connectivity
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            showProgress();
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            showErrorText();
        }


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


    //Overrides of LoaderCallback methods
    @NonNull
    @Override
    public Loader onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri baseUri = Uri.parse(BASE_URL);

        //TODO implement differences for sortby popularity and highest rated

        return new MovieLoader(this, baseUri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        //Set movies to RecyclerView
        mMovieAdapter.setNewsStories(movies);

        //Show Error or Results
        if (movies != null && !movies.isEmpty()) {
            showResults();
        } else {
            showErrorText();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

    }

    //Helper methods for showing results recycler view, progress bar, and error text
    void showResults() {
        mErrorText.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mResultsRecycler.setVisibility(View.VISIBLE);
    }

    void showProgress() {
        mErrorText.setVisibility(View.INVISIBLE);
        mResultsRecycler.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.VISIBLE);
    }

    void showErrorText() {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mResultsRecycler.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }
}
