package com.affogatostudios.movienight.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.affogatostudios.movienight.R;
import com.affogatostudios.movienight.adapters.EndlessRecyclerViewScrollListener;
import com.affogatostudios.movienight.adapters.MovieAdapter;
import com.affogatostudios.movienight.databinding.ActivitySelectionBinding;
import com.affogatostudios.movienight.model.Genre;
import com.affogatostudios.movienight.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectionActivity extends AppCompatActivity {

    private static final String TAG = SelectionActivity.class.getSimpleName();
    private static final String APIKEY = "api_key=935fc3e19d61b30247ef9d015c505c74";
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/";
    private static final String POSTER1 = "https://image.tmdb.org/t/p/w300"; // + size + image.jpg
    private static final String POSTER2 = "https://image.tmdb.org/t/p/w500"; // + size + image.jpg

    private float rating;
    private int voteCount;
    private int releaseDate;
    private boolean isMovie;
    private boolean isTvShow;
    private String[] genres;
    private Integer[] genreMovieIds;
    private Integer[] genreTvIds;
    private ArrayList<Genre> movieGenres;
    private ArrayList<Genre> tvGenres;
    private String[] movieGenreList;
    private String[] tvGenreList;
    private boolean[] movieGenreIsChecked;
    private boolean[] tvGenreIsChecked;
    private ArrayList<Integer> movieIdSearch;
    private ArrayList<Integer> tvIdSearch;
    private boolean isSort = false;

    private String[] sortingOptions = {
            "Popularity Ascending",
            "Popularity Descending",
            "Release Date Ascending",
            "Release Date Descending",
            "Revenue Ascending",
            "Revenue Descending",
            "Average Vote Ascending",
            "Average Vote Descending",
            "Vote Count Ascending",
            "Vote Count Descending",
            "No Sorting"
    };

    private String movie = "movie?";
    private String tvShow = "tv?";
    private String languageEnglish = "&language=en-US";
    private String adultYes = "&include_adult=true";
    private String adultNo = "&include_adult=false";
    // private String genreSearch = "&with_genres=";

    // Search options
    private String releaseDateMinimum = "&release_date.gte=";
    private String ratingMinimum = "&vote_average.gte=";
    private String voteCountMinimum = "&vote_count.gte=";

    // Sorting options
    private String sortByPopularityAscending = "&sort_by=popularity.asc";
    private String sortByPopularityDescending = "&sort_by=popularity.desc";
    private String sortByReleaseAscending = "&sort_by=release_date.asc";
    private String sortByReleaseDescending = "&sort_by=release_date.desc";
    private String sortByRevenueAscending = "&sort_by=revenue.asc";
    private String sortByRevenueDescending = "&sort_by=revenue.desc";
    private String sortByAverageVoteAscending = "&sort_by=vote_average.asc";
    private String sortByAverageVotDescending = "&sort_by=vote_average.desc";
    private String sortByVoteCountAscending = "&sort_by=vote_count.asc";
    private String sortByVoteCountDescending = "&sort_by=vote_count.desc";
    private String url;
    private String sortingOption;

    private int page = 1;
    private String pageNumber = "&page=" + page;

    // private String jsonData;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ActivitySelectionBinding binding;
    private Button sortButton;
    private Button movieGenreButton;
    private Button tvGenreButton;

    private OkHttpClient client;
    private Request request;
    private Request movieRequest;
    private Request tvRequest;
    private List<Movie> movieList;
    private String movieString;
    private String tvString;
    private boolean isSorted;
    private boolean movieStatus;
    private boolean tvStatus;
    private int listSize = 0;

    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        // Get user selections with default values
        Intent intent = getIntent();
        rating = intent.getFloatExtra("rating", 0);
        voteCount = intent.getIntExtra("number_of_ratings", 0);
        releaseDate = intent.getIntExtra("release_date", 0);
        isMovie = intent.getBooleanExtra("movies", false);
        isTvShow = intent.getBooleanExtra("tv_shows", false);
        // ArrayList<String> genreNameArray = intent.getStringArrayListExtra("genre_to_search");
        // genres = genreNameArray.toArray(new String[genreNameArray.size()]);
        // ArrayList<Integer> genreIdArray = intent.getIntegerArrayListExtra("genre_id");
        // genreMovieIds = genreIdArray.toArray(new Integer[genreIdArray.size()]);
        ratingMinimum += rating;
        voteCountMinimum += voteCount;
        releaseDateMinimum += releaseDate;

        movieGenres = new ArrayList<>();
        movieGenres.add(new Genre("Action", 28, false));
        movieGenres.add(new Genre("Adventure", 12, false));
        movieGenres.add(new Genre("Animation", 16, false));
        movieGenres.add(new Genre("Comedy", 35, false));
        movieGenres.add(new Genre("Crime", 80, false));
        movieGenres.add(new Genre("Documentary", 99, false));
        movieGenres.add(new Genre("Drama", 18, false));
        movieGenres.add(new Genre("Family", 10751, false));
        movieGenres.add(new Genre("Fantasy", 14, false));
        movieGenres.add(new Genre("Kids", 10762, false));
        movieGenres.add(new Genre("History", 36, false));
        movieGenres.add(new Genre("Horror", 27, false));
        movieGenres.add(new Genre("Music", 10402, false));
        movieGenres.add(new Genre("Mystery", 9648, false));
        movieGenres.add(new Genre("Romance", 10749, false));
        movieGenres.add(new Genre("Science Fiction", 878, false));
        movieGenres.add(new Genre("TV Movie", 10770, false));
        movieGenres.add(new Genre("Thriller", 53, false));
        movieGenres.add(new Genre("War", 10752, false));
        movieGenres.add(new Genre("Western", 37, false));

        tvGenres = new ArrayList<>();
        tvGenres.add(new Genre("Action & Adventure", 10759, false));
        tvGenres.add(new Genre("Animation", 16, false));
        tvGenres.add(new Genre("Comedy", 35, false));
        tvGenres.add(new Genre("Crime", 80, false));
        tvGenres.add(new Genre("Documentary", 99, false));
        tvGenres.add(new Genre("Drama", 18, false));
        tvGenres.add(new Genre("Family", 10751, false));
        tvGenres.add(new Genre("Kids", 10762, false));
        tvGenres.add(new Genre("Mystery", 9648, false));
        tvGenres.add(new Genre("News", 10763, false));
        tvGenres.add(new Genre("Reality", 10764, false));
        tvGenres.add(new Genre("Sci-Fi & Fantasy", 10765, false));
        tvGenres.add(new Genre("Soap", 10766, false));
        tvGenres.add(new Genre("Talk", 10767, false));
        tvGenres.add(new Genre("War & Politics", 10768, false));
        tvGenres.add(new Genre("Western", 37, false));

        // Search all available genres initially
        movieIdSearch = new ArrayList<>();
        tvIdSearch = new ArrayList<>();

        movieGenreList = new String[movieGenres.size()];
        tvGenreList = new String[tvGenres.size()];
        movieGenreIsChecked = new boolean[movieGenres.size()];
        tvGenreIsChecked = new boolean[tvGenres.size()];

        genreMovieIds = new Integer[movieGenres.size()];
        genreTvIds = new Integer[tvGenres.size()];
        setAllIds();
        /*
        for (int i = 0; i < movieGenreList.length; i++) {
            movieGenreList[i] = movieGenres.get(i).getName();
            movieGenreIsChecked[i] = movieGenres.get(i).isChecked();
            genreMovieIds[i] = movieGenres.get(i).getGenreId();
            movieIdSearch.add(movieGenres.get(i).getGenreId());
        }
        for (int i = 0; i < tvGenres.size(); i++) {
            tvGenreList[i] = tvGenres.get(i).getName();
            tvGenreIsChecked[i] = tvGenres.get(i).isChecked();
            genreTvIds[i] = tvGenres.get(i).getGenreId();
            tvIdSearch.add(tvGenres.get(i).getGenreId());
        }
        */

        sortButton = findViewById(R.id.sortButton);
        movieGenreButton = findViewById(R.id.movieGenreButton);
        movieGenreButton.setVisibility(View.INVISIBLE);
        tvGenreButton = findViewById(R.id.tvGenreButton);
        tvGenreButton.setVisibility(View.INVISIBLE);
        recyclerView = findViewById(R.id.scrollView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MovieAdapter(this);
        recyclerView.setAdapter(adapter);

        // MovieAdapter adapter = new MovieAdapter(movieData(), this);
        // RecyclerView recyclerView = findViewById(R.id.scrollView);
        // recyclerView = findViewById(R.id.scrollView);
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // recyclerView.setAdapter(adapter);

        client = new OkHttpClient();
        movieList = new ArrayList<>();
        movieStatus = false;
        tvStatus = false;
        sortingOption = null;
        isSorted = true;
        movieRequest = new Request.Builder().url(generateMovieUrl(movieIdSearch, pageNumber, sortingOption)).build();
        tvRequest = new Request.Builder().url(generateTvUrl(tvIdSearch, pageNumber, sortingOption)).build();

        if (isMovie) {
            generateMovieCall(movieRequest);
            movieGenreButton.setVisibility(View.VISIBLE);
        }
        if (isTvShow) {
            generateTvCall(tvRequest);
            tvGenreButton.setVisibility(View.VISIBLE);
        }

        movieGenreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieIdSearch.clear();
                // LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                // View inflatedView = inflater.inflate(R.layout.movie_list_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                builder.setTitle("Select Genres");

                builder.setMultiChoiceItems(movieGenreList, movieGenreIsChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isMovie) {
                            if (isChecked) {
                                movieIdSearch.add(movieGenres.get(which).getGenreId());
                            }
                        }
                        /*
                        if (movieGenreIsChecked) {
                            movieIdSearch.add(movieGenres.get(which).getGenreId());
                        }
                        */
                    }
                });

                // builder.setView(inflatedView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (movieIdSearch.isEmpty()) {
                            setAllIds();
                        }
                        adapter.clearMovies();
                        if (isMovie) {
                            movieRequest = new Request.Builder().url(generateMovieUrl(movieIdSearch, pageNumber, sortingOption)).build();
                            refreshMovieCall(movieRequest);
                        }
                        if (isTvShow) {
                            tvRequest = new Request.Builder().url(generateTvUrl(tvIdSearch, pageNumber, sortingOption)).build();
                            refreshTvCall(tvRequest);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        movieIdSearch.clear();
                        for (int i = 0; i < movieGenreIsChecked.length; i++) {
                            movieGenreIsChecked[i] = false;
                        }
                    }
                });
                builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < movieGenreIsChecked.length; i++) {
                            movieGenreIsChecked[i] = true;
                        }
                    }
                });
                // AlertDialog dialog = builder.create();
                builder.show();
            }
        });

        tvGenreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIdSearch.clear();
                // LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                // View inflatedView = inflater.inflate(R.layout.movie_list_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                builder.setTitle("Select Genres");

                builder.setMultiChoiceItems(tvGenreList, tvGenreIsChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isTvShow) {
                            if (isChecked) {
                                movieIdSearch.add(tvGenres.get(which).getGenreId());
                            }
                        }
                        /*
                        if (movieGenreIsChecked) {
                            movieIdSearch.add(movieGenres.get(which).getGenreId());
                        }
                        */
                    }
                });

                // builder.setView(inflatedView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tvIdSearch.isEmpty()) {
                            setAllIds();
                        }
                        adapter.clearMovies();
                        if (isTvShow) {
                            tvRequest = new Request.Builder().url(generateTvUrl(tvIdSearch, pageNumber, sortingOption)).build();
                            refreshTvCall(tvRequest);
                        }
                        if (isMovie) {
                            movieRequest = new Request.Builder().url(generateMovieUrl(movieIdSearch, pageNumber, sortingOption)).build();
                            refreshMovieCall(movieRequest);
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvIdSearch.clear();
                        for (int i = 0; i < tvGenreIsChecked.length; i++) {
                            tvGenreIsChecked[i] = false;
                        }
                    }
                });
                builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < tvGenreIsChecked.length; i++) {
                            tvGenreIsChecked[i] = true;
                        }
                    }
                });
                // AlertDialog dialog = builder.create();
                builder.show();
            }
        });

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectionActivity.this);
                builder.setTitle("Select Sorting Option");

                builder.setSingleChoiceItems(sortingOptions, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                sortingOption = sortByPopularityAscending;
                                break;
                            case 1:
                                sortingOption = sortByPopularityDescending;
                                break;
                            case 2:
                                sortingOption = sortByReleaseAscending;
                                break;
                            case 3:
                                sortingOption = sortByReleaseDescending;
                                break;
                            case 4:
                                sortingOption = sortByRevenueAscending;
                                break;
                            case 5:
                                sortingOption = sortByRevenueDescending;
                                break;
                            case 6:
                                sortingOption = sortByAverageVoteAscending;
                                break;
                            case 7:
                                sortingOption = sortByAverageVotDescending;
                                break;
                            case 8:
                                sortingOption = sortByVoteCountAscending;
                                break;
                            case 9:
                                sortingOption = sortByVoteCountDescending;
                                break;
                            case 10:
                                sortingOption = null;
                                break;
                        }
                    }
                });

                // builder.setView(inflatedView);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.clearMovies();
                        if (isMovie) {
                            movieRequest = new Request.Builder().url(generateMovieUrl(movieIdSearch, pageNumber, sortingOption)).build();
                            refreshMovieCall(movieRequest);
                        }
                        if (isTvShow) {
                            tvRequest = new Request.Builder().url(generateTvUrl(tvIdSearch, pageNumber, sortingOption)).build();
                            refreshTvCall(tvRequest);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // isSort = false;
                    }
                });
                builder.show();
            }
        });

        // Retain an instance so that you can call resetState() for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadMoreData();
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void setAllIds() {
        for (int i = 0; i < movieGenreList.length; i++) {
            movieGenreList[i] = movieGenres.get(i).getName();
            movieGenreIsChecked[i] = movieGenres.get(i).isChecked();
            genreMovieIds[i] = movieGenres.get(i).getGenreId();
            movieIdSearch.add(movieGenres.get(i).getGenreId());
        }
        for (int i = 0; i < tvGenres.size(); i++) {
            tvGenreList[i] = tvGenres.get(i).getName();
            tvGenreIsChecked[i] = tvGenres.get(i).isChecked();
            genreTvIds[i] = tvGenres.get(i).getGenreId();
            tvIdSearch.add(tvGenres.get(i).getGenreId());
        }
    }

    private void loadMoreData() {
        this.page++;
        pageNumber = "&page=" + this.page;
        if (isMovie) {
            movieRequest = new Request.Builder().url(generateMovieUrl(movieIdSearch, pageNumber, sortingOption)).build();
            generateMovieCall(movieRequest);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), listSize);
        }
        if (isTvShow) {
            tvRequest = new Request.Builder().url(generateTvUrl(tvIdSearch, pageNumber, sortingOption)).build();
            generateTvCall(tvRequest);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), listSize);
        }
        // adapter.notifyDataSetChanged();
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    private void refreshMovieCall(Request request) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        // movieList.addAll(getMovieData(jsonData));
                        // adapter = new MovieAdapter(movieList,SelectionActivity.this);
                        adapter.addMovies(getMovieData(jsonData));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        scrollListener.resetState();
    }

    private void refreshTvCall(Request request) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        // movieList.addAll(getMovieData(jsonData));
                        // adapter = new MovieAdapter(movieList,SelectionActivity.this);
                        adapter.addMovies(getTvData(jsonData));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        scrollListener.resetState();
    }

    private void generateMovieCall(Request request) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        // movieList.addAll(getMovieData(jsonData));
                        // adapter = new MovieAdapter(movieList,SelectionActivity.this);
                        adapter.addMovies(getMovieData(jsonData));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRangeInserted(adapter.getItemCount(), 20);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void generateTvCall(Request request) {
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String jsonData = response.body().string();
                    if (response.isSuccessful()) {
                        // movieList.addAll(getTvData(jsonData));
                        // adapter = new MovieAdapter(movieList,SelectionActivity.this);
                        adapter.addMovies(getTvData(jsonData));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemRangeInserted(adapter.getItemCount(), 20);
                            }
                        });
                    }
                } catch (IOException e) {

                } catch (JSONException e) {
                    Log.d(TAG, "Object not found");
                }
            }
        });
    }

    private List<Movie> getMovieData(String jsonData) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        JSONObject movieObject = new JSONObject(jsonData);
        JSONArray movieArray = movieObject.getJSONArray("results");
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject object = movieArray.getJSONObject(i);
            Movie movie = new Movie(object.getString("title"),
                    convertMovieIdToGenre(object),
                    object.getString("overview"),
                    (float) object.getDouble("vote_average"),
                    object.getInt("vote_count"),
                    object.getString("release_date"),
                    POSTER1 + object.getString("poster_path"),
                    POSTER2 + object.getString("poster_path"));
            movies.add(movie);
        }
        listSize = movies.size();
        return movies;
    }

    private List<Movie> getTvData(String jsonData) throws JSONException {
        List<Movie> tvs = new ArrayList<>();
        JSONObject tvObject = new JSONObject(jsonData);
        JSONArray tvArray = tvObject.getJSONArray("results");
        for (int i = 0; i < tvArray.length(); i++) {
            JSONObject object = tvArray.getJSONObject(i);
            Movie tv = new Movie(object.getString("name"),
                    convertTvIdToGenre(object),
                    object.getString("overview"),
                    (float) object.getDouble("vote_average"),
                    object.getInt("vote_count"),
                    object.getString("first_air_date"),
                    POSTER1 + object.getString("poster_path"),
                    POSTER2 + object.getString("poster_path"));
            tvs.add(tv);
        }
        listSize = tvs.size();
        return tvs;
    }

    private String convertMovieIdToGenre(JSONObject object) throws JSONException {
        JSONArray genreArray = object.getJSONArray("genre_ids");
        String genreString = "";
        for (int i = 0; i < genreArray.length(); i++) {
            switch (genreArray.getInt(i)) {
                case 28:
                    genreString += "Action";
                    break;
                case 12:
                    genreString += "Adventure";
                    break;
                case 16:
                    genreString += "Animation";
                    break;
                case 35:
                    genreString += "Comedy";
                    break;
                case 80:
                    genreString += "Crime";
                    break;
                case 99:
                    genreString += "Documentary";
                    break;
                case 18:
                    genreString += "Drama";
                    break;
                case 10751:
                    genreString += "Family";
                    break;
                case 14:
                    genreString += "Fantasy";
                    break;
                case 36:
                    genreString += "History";
                    break;
                case 27:
                    genreString += "Horror";
                    break;
                case 10402:
                    genreString += "Music";
                    break;
                case 9648:
                    genreString += "Mystery";
                    break;
                case 10749:
                    genreString += "Romance";
                    break;
                case 878:
                    genreString += "Science Fiction";
                    break;
                case 10770:
                    genreString += "TV Movie";
                    break;
                case 53:
                    genreString += "Thriller";
                    break;
                case 10752:
                    genreString += "War";
                    break;
                case 37:
                    genreString += "Western";
                    break;
            }
            genreString += ", ";
        }
        // genreString = genreString.substring(0, genreString.length() - 2);
        Log.d(TAG, genreString);
        return genreString;
    }

    private String convertTvIdToGenre(JSONObject object) throws JSONException {
        JSONArray genreArray = object.getJSONArray("genre_ids");
        // JSONArray genreArray = array;
        String genreString = "";
        for (int i = 0; i < genreArray.length(); i++) {
            switch (genreArray.getInt(i)) {
                case 10759:
                    genreString += "Action & Adventure";
                    break;
                case 16:
                    genreString += "Animation";
                    break;
                case 35:
                    genreString += "Comedy";
                    break;
                case 80:
                    genreString += "Crime";
                    break;
                case 99:
                    genreString += "Documentary";
                    break;
                case 18:
                    genreString += "Drama";
                    break;
                case 10751:
                    genreString += "Family";
                    break;
                case 10762:
                    genreString += "Kids";
                    break;
                case 9648:
                    genreString += "Mystery";
                    break;
                case 10763:
                    genreString += "News";
                    break;
                case 10764:
                    genreString += "Reality";
                    break;
                case 10765:
                    genreString += "Sci-Fi & Fantasy";
                    break;
                case 10766:
                    genreString += "Soap";
                    break;
                case 10767:
                    genreString += "Talk";
                    break;
                case 10768:
                    genreString += "War & Politics";
                    break;
                case 37:
                    genreString += "Western";
                    break;
            }
            genreString += ", ";
        }
        // genreString = genreString.substring(0, genreString.length() - 2);
        Log.d(TAG, genreString);
        return genreString;
    }

    private String generateMovieUrl(ArrayList<Integer> genres, String page, String sort) {
        String genreSearch = "&with_genres=";
        if (sort != null) {
            Log.d(TAG, "Not null");
            sortingOption = sort;
        }
        for (int i = 0; i < genres.size(); i++) {
            genreSearch += genres.get(i) + "|";
        }
        String url = BASE_URL + this.movie + APIKEY + ratingMinimum  + voteCountMinimum + releaseDateMinimum  + genreSearch + sortingOption + page;
        Log.d(TAG, url);
        return url;
    }

    /*
    private String generateMovieUrl(Integer[] genres, String page, String sort) {
        String genreSearch = "&with_genres=";
        if (sort != null) {
            Log.d(TAG, "Not null");
            sortingOption = sort;
        }
        for (int i = 0; i < genres.length; i++) {
            genreSearch += genres[i] + "|";
        }
        String url = BASE_URL + this.movie + APIKEY + ratingMinimum  + voteCountMinimum + releaseDateMinimum  + genreSearch + sortingOption + page;
        Log.d(TAG, url);
        return url;
    }
    */

    private String generateTvUrl(ArrayList<Integer> genres, String page, String sort) {
        String genreSearch = "&with_genres=";
        if (sort != null) {
            Log.d(TAG, "Not null");
            sortingOption = sort;
        }
        for (int i = 0; i < genres.size(); i++) {
            genreSearch += genres.get(i) + "|";
        }
        String url = BASE_URL + this.tvShow + APIKEY + ratingMinimum  + voteCountMinimum  + releaseDateMinimum  + genreSearch + sortingOption + page;
        Log.d(TAG, url);
        return url;
    }

    /*
    private String generateTvUrl(Integer[] genres, String page, String sort) {
        String genreSearch = "&with_genres=";
        if (sort != null) {
            Log.d(TAG, "Not null");
            sortingOption = sort;
        }
        for (int i = 0; i < genres.length; i++) {
            genreSearch += genres[i] + "|";
        }
        String url = BASE_URL + this.tvShow + APIKEY + ratingMinimum  + voteCountMinimum  + releaseDateMinimum  + genreSearch + sortingOption + page;
        Log.d(TAG, url);
        return url;
    }
    */
}
