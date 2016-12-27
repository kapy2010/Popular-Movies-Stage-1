package com.example.android.popular_movies_stage_1;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popular_movies_stage_1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kapil on 25/12/16.
 */

public class MovieAdaptor extends ArrayAdapter<Movie> {
    public MovieAdaptor(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = getContext();
        final int width = context.getResources().getDisplayMetrics().widthPixels;
        final int height = context.getResources().getDisplayMetrics().heightPixels;

        final Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_tile, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);

        String imagePath = movie.getPosterPath();

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.with(context).load(NetworkUtils.buildImageUrl(imagePath))
                    .centerCrop()
                    .resize(height / 2, width / 2)
                    .into(imageView);
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Picasso.with(context).load(NetworkUtils.buildImageUrl(imagePath))
                    .centerCrop()
                    .resize(width / 2, height / 2)
                    .into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class destinationActivity = MovieDetails.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);
                startChildActivityIntent.putExtra("movie", movie);
                startChildActivityIntent.putExtra("width", width);
                startChildActivityIntent.putExtra("height", height);

                startChildActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(startChildActivityIntent);
            }
        });

        return convertView;
    }
}
