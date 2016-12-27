package com.example.android.popular_movies_stage_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popular_movies_stage_1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    private TextView mMovieTitle;
    private ImageView mMoviePoster;
    private TextView mMovieOverview;
    private TextView mMovieRating;
    private TextView mMovieReleaseDate;

    Context context = MovieDetails.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.movie_detail);
        setContentView(R.layout.activity_movie_details);

        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieOverview = (TextView) findViewById(R.id.movie_overview);
        mMovieRating = (TextView) findViewById(R.id.movie_rating);
        mMovieReleaseDate = (TextView) findViewById(R.id.movie_release_date);

        Intent intentThatStartsThisActivity = getIntent();

        if (intentThatStartsThisActivity.hasExtra("movie")) {
            Movie movie = intentThatStartsThisActivity.getExtras().getParcelable("movie");
            int height = intentThatStartsThisActivity.getExtras().getInt("height");
            int width = intentThatStartsThisActivity.getExtras().getInt("width");

            mMovieTitle.setText(movie.getOriginalTitle());

            Picasso.with(context).load(NetworkUtils.buildImageUrl(movie.getPosterPath()))
                    .centerCrop()
                    .resize(width / 3, height / 3)
                    .into(mMoviePoster);

            mMovieOverview.setText(movie.getPlotSynopsis());

            mMovieRating.setText(movie.getUserRating() + "/10");

            mMovieReleaseDate.setText(movie.getReleaseDate());
        }

    }
}
