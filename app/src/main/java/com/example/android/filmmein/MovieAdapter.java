package com.example.android.filmmein;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> mMovies;

    MovieAdapter() {

    }



    //Inner ViewHolder Class
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView mPosterView;

        //Constructor
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            mPosterView = (ImageView) itemView.findViewById(R.id.recycler_poster_iv);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIDForListItem = R.layout.movie_poster_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIDForListItem, viewGroup, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        Movie currentMovie = mMovies.get(position);

        String posterImageLink = currentMovie.getPosterImageLink();
        ImageView posterImageView = movieViewHolder.mPosterView;

        Picasso.get()
                .load(posterImageLink)
                .into(posterImageView);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    /**
     * Helper method to set Movie List to existing MovieAdapter to prevent creating a new one
     *
     * @param movies the new set of movies to be displayed
     */

    public void setNewsStories(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
