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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickHandler {
    /*********************************************
     * The main class that displays a Recycler   *
     * full of Movie Posters to be clicked on    *
     *********************************************/


    //TEST VARIABLES
    private final String TEST_URL_STRING = "https://api.themoviedb.org/3/movie/550?api_key=47ba04034a3ebe6cffa7dbc0a0ae4dd8";

    private final String TEST_JSON_STRING = "{\"adult\":false,\"backdrop_path\":\"/87hTDiay2N2qWyX4Ds7ybXi9h8I.jpg\",\"belongs_to_collection\":null,\"budget\":63000000,\"genres\":[{\"id\":18,\"name\":\"Drama\"}],\"homepage\":\"http://www.foxmovies.com/movies/fight-club\",\"id\":550,\"imdb_id\":\"tt0137523\",\"original_language\":\"en\",\"original_title\":\"Fight Club\",\"overview\":\"A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.\",\"popularity\":26.112,\"poster_path\":\"/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg\",\"production_companies\":[{\"id\":508,\"logo_path\":\"/7PzJdsLGlR7oW4J0J5Xcd0pHGRg.png\",\"name\":\"Regency Enterprises\",\"origin_country\":\"US\"},{\"id\":711,\"logo_path\":\"/tEiIH5QesdheJmDAqQwvtN60727.png\",\"name\":\"Fox 2000 Pictures\",\"origin_country\":\"US\"},{\"id\":20555,\"logo_path\":null,\"name\":\"Taurus Film\",\"origin_country\":\"\"},{\"id\":54051,\"logo_path\":null,\"name\":\"Atman Entertainment\",\"origin_country\":\"\"},{\"id\":54052,\"logo_path\":null,\"name\":\"Knickerbocker Films\",\"origin_country\":\"US\"},{\"id\":25,\"logo_path\":\"/qZCc1lty5FzX30aOCVRBLzaVmcp.png\",\"name\":\"20th Century Fox\",\"origin_country\":\"US\"},{\"id\":4700,\"logo_path\":\"/A32wmjrs9Psf4zw0uaixF0GXfxq.png\",\"name\":\"The Linson Company\",\"origin_country\":\"\"}],\"production_countries\":[{\"iso_3166_1\":\"DE\",\"name\":\"Germany\"},{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"1999-10-15\",\"revenue\":100853753,\"runtime\":139,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"Mischief. Mayhem. Soap.\",\"title\":\"Fight Club\",\"video\":false,\"vote_average\":8.4,\"vote_count\":14379}";

    private final Movie TEST_MOVIE = new Movie("Fight Club", "1999-10-15", "http://image.tmdb.org/t/p/original/adw6Lq9FiC9zjYEpOqfq03ituwp.jpg", 8.4, "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \\\"fight clubs\\\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.");

    private final ArrayList<Movie> TEST_MOVIE_ARRAY = generateMoviesArray();

    private ArrayList<Movie> generateMoviesArray(){
        ArrayList<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            movies.add(TEST_MOVIE);
        }
        return movies;
    }
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
        mMovieAdapter.setNewsStories(TEST_MOVIE_ARRAY);


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
        detailIntent.putExtra("movie", movie);
        startActivity(detailIntent);
    }
}
