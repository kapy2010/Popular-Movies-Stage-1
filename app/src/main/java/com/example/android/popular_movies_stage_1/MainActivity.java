package com.example.android.popular_movies_stage_1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movies_stage_1.utilities.MovieJsonUtils;
import com.example.android.popular_movies_stage_1.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String NOW_PLAYING_PATH = "now_playing";
    public static final String POPULAR_PATH = "popular";
    public static final String TOP_RATED_PATH = "top_rated";

    private GridView mMovieGridView;
    private ProgressBar mLoadingIndicator;
    private MovieAdaptor movieAdaptor;
    private TextView mErrorMessageDisplay;
    private Button mRefreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieGridView = (GridView) findViewById(R.id.movie_gridview);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        mRefreshButton = (Button) findViewById(R.id.refresh_button);
        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadMovieData();
            }
        });

        loadMovieData();
    }

    private void loadMovieData() {
        new FetchMovieTask().execute(NOW_PLAYING_PATH);
    }

    private void loadPopularMovieData() {
        new FetchMovieTask().execute(POPULAR_PATH);
    }

    private void loadTopRatedMovieData() {
        new FetchMovieTask().execute(TOP_RATED_PATH);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showErrorMessage() {
        mMovieGridView.setVisibility(View.INVISIBLE);
        Context context = getApplicationContext();
        int height = context.getResources().getDisplayMetrics().heightPixels;
        mErrorMessageDisplay.setHeight(height / 2);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.VISIBLE);
    }

    private void showMovieGrid() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRefreshButton.setVisibility(View.INVISIBLE);
        mMovieGridView.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        mMovieGridView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mMovieGridView.setVisibility(View.VISIBLE);
    }

    class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMovieGrid();
            showProgressBar();
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String path = params[0];

            if (isOnline()) {
                URL movieRequestUrl = NetworkUtils.buildUrl(path);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(movieRequestUrl);

                    List<Movie> moviesList = MovieJsonUtils
                            .getMovieStringsFromJson(jsonMovieResponse);

                    return moviesList;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> moviesList) {
            hideProgressBar();

            if (moviesList != null) {
                movieAdaptor = new MovieAdaptor(getApplicationContext(), moviesList);
                mMovieGridView.setAdapter(movieAdaptor);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.most_popular) {
            loadPopularMovieData();
            return true;
        } else if (id == R.id.top_rated) {
            loadTopRatedMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

