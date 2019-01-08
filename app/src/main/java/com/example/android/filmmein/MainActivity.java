package com.example.android.filmmein;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.util.Log;
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

import java.util.ArrayList;
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
    private List<Movie> mMovieResults;

    private final String LOG_TAG = getClass().getSimpleName();

    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    private final int LOADER_ID = 77;

    //String to keep current value of Sort By Spinner
    private String mSavedSortBySelection;

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

        mResultsRecycler.addItemDecoration(new MovieRecyclerDecoration(this, numberOfColumns));

        //Set Spinner options
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_by_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSortBySpinner.setAdapter(spinnerAdapter);



        //Check for saved movie results
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(getString(R.string.movie_results_key))) {
                //Set movies if saved list exists
                List<Movie> savedMovies = savedInstanceState.getParcelableArrayList(getString(R.string.movie_results_key));
                mMovieAdapter.setMovies(savedMovies);
                mMovieResults = savedMovies;
            } else {
                //Else check network status and initiate or restart Loader
                checkNetworkStatusAndExecute();
            }
            //Attempt to restore get value of spinner from SavedInstanceState for comparison in OnItemSelectedListener
            if (savedInstanceState.containsKey(getString(R.string.spinner_value_key))) {
                mSavedSortBySelection = savedInstanceState.getString(getString(R.string.spinner_value_key));
            }
        } else {
            //Else check network status and initiate or restart Loader
            checkNetworkStatusAndExecute();
            //On Initial Creation, save value of spinner for comparison in OnItemSelectedListener
            mSavedSortBySelection = mSortBySpinner.getSelectedItem().toString();
        }


        //Set OnItemSelectedListener to spinner
        mSortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newlySelectedSortBySelection = mSortBySpinner.getSelectedItem().toString();
                //This if-else is necessary to prevent unnecessary Loader requests when loading from savedInstanceState
                //Compare spinner value to currently saved String
                if (!(newlySelectedSortBySelection.equals(mSavedSortBySelection)) && !(newlySelectedSortBySelection.equals(getString(R.string.favorites)))) {
                    checkNetworkStatusAndExecute(); //Create new loader request if it is has changed to one of the values that requires internet querying (i.e. user has selected a new value other than favorites)
                    mSavedSortBySelection = newlySelectedSortBySelection;
                } else if (newlySelectedSortBySelection.equals(getString(R.string.favorites))) { //Else, do not make a Loader request, and load favorites if selected (ViewModel/LiveData will prevent unnecessary queries)
                    setupViewModel();
                    mSavedSortBySelection = newlySelectedSortBySelection;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Add refresh capability to error screen
        mErrorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkStatusAndExecute();
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
        switch (item.getItemId()) {
            case (R.id.action_about):
                Intent aboutIntent = new Intent(MainActivity.this, AboutActivity.class);
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
            builder.appendPath("popular");
        } else if (sortBy.equals(getString(R.string.highest_rated))) {
            builder.appendPath("top_rated");
        }

        builder.appendQueryParameter(getString(R.string.api_key_key), getString(R.string.api_key));

        Uri builtUri = builder.build();

        return new MovieLoader(this, builtUri.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {

        //Set movies to RecyclerView unless on Favorites tab.
        if (!(mSortBySpinner.getSelectedItem().toString().equals(getString(R.string.favorites)))) {
            mMovieAdapter.setMovies(movies);
        } else {
            return;
        }

        //Return if currently set to favorites, as load is unnecessary, otherwise show Error or Results
        if (movies != null && !movies.isEmpty()) {
            mMovieResults = movies; //Save current set of movies as member variable
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

    //Helper method to check network status and initiate loader or otherwise show an error
    void checkNetworkStatusAndExecute() {
        //Check network connectivity
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            showProgress();
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        } else {
            showErrorText();
        }
    }

    //SaveInstanceState logic
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save current Movies
        outState.putParcelableArrayList(getString(R.string.movie_results_key), (ArrayList<Movie>) mMovieResults);
        //Save value of spinner
        String currentSpinnerValue = mSortBySpinner.getSelectedItem().toString();
        outState.putString(getString(R.string.spinner_value_key), currentSpinnerValue);
    }

    //Helper method to make calls to the Database
    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieResults = movies;
                mMovieAdapter.setMovies(movies);
            }
        });
    }
}
