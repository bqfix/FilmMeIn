package com.example.android.filmmein;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.filmmein.database.AppDatabase;
import com.example.android.filmmein.database.AppExecutors;
import com.squareup.picasso.Picasso;

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

    private AppDatabase mdb;

    private boolean mFavorited = false;

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

        //Get Database Instance
        mdb = AppDatabase.getInstance(this);



        //Check orientation to set height of Poster
        Resources resources = this.getResources();
        if (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPosterView.getLayoutParams().height = (int) resources.getDimension(R.dimen.detail_poster_height_landscape);
        } else {
            mPosterView.getLayoutParams().height = (int) resources.getDimension(R.dimen.detail_poster_height_portrait);
        } mPosterView.requestLayout(); //To refresh the layout regardless

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
                if (!mFavorited){
                    insertFavoriteMovie();
                    Toast.makeText(v.getContext(), R.string.movie_added, Toast.LENGTH_SHORT).show();
                } else if (mFavorited) {
                    deleteFavoriteMovie();
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
                mdb.movieDao().insertMovie(mMovie);
            }
        });
    }

    //Helper method to create executor to delete favorite movie
    private void deleteFavoriteMovie() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mdb.movieDao().deleteMovie(mMovie);
            }
        });
    }
}
