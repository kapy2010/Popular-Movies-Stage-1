package com.example.android.popular_movies_stage_1.utilities;

import com.example.android.popular_movies_stage_1.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kapil on 25/12/16.
 */

public final class MovieJsonUtils {

    private static String inFormat = "yyyy-MM-dd";
    private static String outFormat = "MMM d, yyyy";

    private static final String TMDB_RESULTS = "results";
    private static final String TMDB_ID = "id";
    private static final String TMDB_ORIGINAL_TITLE = "original_title";
    private static final String TMDB_POSTER_PATH = "poster_path";
    private static final String TMDB_OVERVIEW = "overview";
    private static final String TMDB_VOTE_AVERAGE = "vote_average";
    private static final String TMDB_RELEASE_DATE = "release_date";

    public static List<Movie> getMovieStringsFromJson(String movieJsonStr) throws JSONException {

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray results = movieJson.getJSONArray(TMDB_RESULTS);

        List<Movie> moviesList = new ArrayList<Movie>();

        for (int i = 0; i < results.length(); i++) {

            JSONObject movieObj = results.getJSONObject(i);

            int movieId = movieObj.getInt(TMDB_ID);
            String originalTitle = movieObj.getString(TMDB_ORIGINAL_TITLE);
            String posterPath = movieObj.getString(TMDB_POSTER_PATH).substring(1);
            String overview = movieObj.getString(TMDB_OVERVIEW);
            String voteAverage = movieObj.getString(TMDB_VOTE_AVERAGE);
            String releaseDate = null;
            try {
                releaseDate = getReadableDate(movieObj.getString(TMDB_RELEASE_DATE));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            moviesList.add(new Movie(movieId, originalTitle, posterPath,
                    overview, voteAverage, releaseDate));
        }

        return moviesList;
    }

    private static String getReadableDate(String date) throws ParseException {
        Date inDate = new SimpleDateFormat(inFormat).parse(date);
        String outDate = new SimpleDateFormat(outFormat).format(inDate);
        return outDate;
    }
}
