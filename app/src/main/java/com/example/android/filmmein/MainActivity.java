package com.example.android.filmmein;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

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
    private LinearLayout mRecyclerLayout;
    private Spinner mSortBySpinner;

    private final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?api_key=47ba04034a3ebe6cffa7dbc0a0ae4dd8&language=en-US&include_adult=false&include_video=false&page=1";

    private final int LOADER_ID = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign member variables
        mResultsRecycler = (RecyclerView) findViewById(R.id.movie_rv);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);
        mErrorText = (TextView) findViewById(R.id.error_text);
        mRecyclerLayout = (LinearLayout) findViewById(R.id.recycler_view_layout);
        mSortBySpinner = (Spinner) findViewById(R.id.sort_by_spinner);


        //Measure screen size and determine number of columns that will fit
        int numberOfColumns = Utilities.getNumberOfColumns(this);

        //Logic to Setup RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns, LinearLayoutManager.VERTICAL, false);
        mResultsRecycler.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(this);
        mResultsRecycler.setAdapter(mMovieAdapter);

        //TODO Create custom ItemDecoration to space grid items better

        //Set Spinner options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.sort_by_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(spinnerAdapter);

        //Check network connectivity
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            showProgress();
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            showErrorText();
        }


        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSupportLoaderManager().restartLoader(LOADER_ID,null,MainActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        Uri.Builder builder = baseUri.buildUpon();
        String sortBy = mSortBySpinner.getSelectedItem().toString();

        if (sortBy.equals(getString(R.string.most_popular))) {
            builder.appendQueryParameter("sort_by", "popularity.desc");
        } else if (sortBy.equals(getString(R.string.highest_rated))) {
            builder.appendQueryParameter("sort_by", "vote_average.desc");
        }

        Uri builtUri = builder.build();


        return new MovieLoader(this, builtUri.toString());
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

    //Helper methods for showing results recycler view layout, progress bar, and error text
    void showResults() {
        mErrorText.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mRecyclerLayout.setVisibility(View.VISIBLE);
    }

    void showProgress() {
        mErrorText.setVisibility(View.INVISIBLE);
        mRecyclerLayout.setVisibility(View.INVISIBLE);
        mProgressIndicator.setVisibility(View.VISIBLE);
    }

    void showErrorText() {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mRecyclerLayout.setVisibility(View.INVISIBLE);
        mErrorText.setVisibility(View.VISIBLE);
    }

    //TODO implement onSavedInstanceState
}
