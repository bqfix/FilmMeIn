package com.example.android.filmmein;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmmein.database.AppDatabase;
import com.example.android.filmmein.database.AppExecutors;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    /*********************************************
     * The activity shown when a poster is       *
     * clicked in the main activity              *
     *********************************************/

    //Member Variables
    private Movie mMovie = null;
    private ImageView mPosterView;
    private TextView mReleaseDate;
    private TextView mVoterAverage;
    private TextView mSynopsis;
    private FloatingActionButton mFavoriteButton;
    private Button mTrailerButton;

    private AppDatabase mDb;

    private Movie mDatabaseMovie;

    private boolean mIsFavorited = false;

    private final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    private String mMovieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //Attempt to retrieve Movie object from intent extras
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.intent_extra_key))) {
            mMovie = intent.getParcelableExtra(getString(R.string.intent_extra_key));
        }

        //Abandon if Movie Data unavailable
        if (mMovie == null) {
            finish();
            Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
        }

        //If successful, set views
        mPosterView = (ImageView) findViewById(R.id.detail_poster_iv);
        mReleaseDate = (TextView) findViewById(R.id.release_date_tv);
        mVoterAverage = (TextView) findViewById(R.id.voter_average_tv);
        mSynopsis = (TextView) findViewById(R.id.synopsis_tv);
        mFavoriteButton = (FloatingActionButton) findViewById(R.id.favorite_fab);
        mTrailerButton = (Button) findViewById(R.id.trailer_button);

        //Get Database Instance
        mDb = AppDatabase.getInstance(this);

        //Check if movie is in database by attempting to query the database for it via ID
        DetailViewModelFactory factory = new DetailViewModelFactory(mDb, mMovie.getId());
        final DetailViewModel viewModel
                = ViewModelProviders.of(this, factory).get(DetailViewModel.class);

        viewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                viewModel.getMovie().removeObserver(this);
                mDatabaseMovie = movie;
                if (mDatabaseMovie != null) {
                    movieFavorited();
                }
            }
        });

        //Set movie ID in preparation for following methods
        mMovieID = String.valueOf(mMovie.getId());

        //Get trailer's Uri
        Uri videosUri = createVideosUri();
        final URL videosURL = Utilities.buildURL(videosUri.toString());



        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String videosJSON = Utilities.makeHttpRequest(videosURL);
                final Uri trailerUri = Utilities.parseMovieForTrailer(DetailActivity.this, videosJSON);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Set trailer button to open an intent using the previously parsed Uri
                        mTrailerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Check to make sure the link isn't dead
                                if (!(trailerUri.toString().equals(Utilities.BASE_YOUTUBE_URL))) {
                                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
                                    startActivity(trailerIntent);
                                } else {
                                    Toast.makeText(DetailActivity.this, getString(R.string.no_youtube_video), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });




        //Get trailer's reviews
        Uri reviewsUri = createReviewsUri();
        final URL reviewsURL = Utilities.buildURL(reviewsUri.toString());
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String reviewsJSON = Utilities.makeHttpRequest(reviewsURL);
                List<String> reviews = Utilities.parseMovieForReviews(DetailActivity.this, reviewsJSON);
            }
        });


        //TODO create simple listview to display reviews



        //Check orientation to set height of Poster
        Resources resources = this.getResources();
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPosterView.getLayoutParams().height = (int) resources.getDimension(R.dimen.detail_poster_height_landscape);
        } else {
            mPosterView.getLayoutParams().height = (int) resources.getDimension(R.dimen.detail_poster_height_portrait);
        }
        mPosterView.requestLayout(); //To refresh the layout regardless

        //Set view contents from parceled movie
        setTitle(mMovie.getTitle());
        Picasso.get()
                .load(mMovie.getPosterImageLink())
                .into(mPosterView);
        mReleaseDate.setText(mMovie.getReleaseDate());
        mVoterAverage.setText(Double.toString(mMovie.getVoterAverage()));
        mSynopsis.setText(mMovie.getPlotSynopsis());

        //Set onclicklistener to favorite button
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsFavorited) {
                    insertFavoriteMovie();
                    movieFavorited();
                    Toast.makeText(v.getContext(), R.string.movie_added, Toast.LENGTH_SHORT).show();
                } else if (mIsFavorited) {
                    deleteFavoriteMovie();
                    movieUnfavorited();
                    Toast.makeText(v.getContext(), R.string.movie_deleted, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Helper method to create executor to insert favorite movie
    private void insertFavoriteMovie() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(mMovie);
            }
        });
    }

    //Helper method to create executor to delete favorite movie
    private void deleteFavoriteMovie() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(mMovie);
            }
        });
    }

    //Helper method to call when a movie is favorited
    private void movieFavorited() {
        mIsFavorited = true;
        mFavoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.star_on));
    }

    //Helper method to call when a movie is unfavorited
    private void movieUnfavorited() {
        mIsFavorited = false;
        mFavoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.star_off));
    }

    //Helper method to create Uri to access the JSON for the movies' videos
    private Uri createVideosUri() {
        Uri videoUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = videoUri.buildUpon();
        builder.appendPath(mMovieID);
        builder.appendPath(getString(R.string.videos));
        builder.appendQueryParameter(getString(R.string.api_key_key), getString(R.string.api_key));

        return builder.build();
    }

    //Helper method to create Uri to access the JSON for the movies' reviews
    private Uri createReviewsUri() {
        Uri videoUri = Uri.parse(BASE_MOVIE_URL);
        Uri.Builder builder = videoUri.buildUpon();
        builder.appendPath(mMovieID);
        builder.appendPath(getString(R.string.reviews));
        builder.appendQueryParameter(getString(R.string.api_key_key), getString(R.string.api_key));

        return builder.build();
    }
}
