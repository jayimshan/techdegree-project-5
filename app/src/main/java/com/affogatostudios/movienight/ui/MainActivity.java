package com.affogatostudios.movienight.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.affogatostudios.movienight.R;
import com.affogatostudios.movienight.model.Genre;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView ratingTextView;
    private SeekBar ratingSeekBar;
    private TextView ratingResultTextView;
    private TextView numberOfRatingsTextView;
    private SeekBar numberOfRatingsSeekBar;
    private TextView numberOfRatingsResultTextView;
    private TextView releaseDateTextView;
    private SeekBar releaseDateSeekBar;
    private TextView releaseDateResultTextView;
    private CheckBox moviesCheckBox;
    private CheckBox tvShowsCheckBox;
    private boolean isMovieChecked;
    private boolean isTvShowChecked;
    // private Button genreButton;
    private Button searchButton;

    private DecimalFormat df;

    private float ratingSelection;
    private int numberOfRatingsSelection;
    private int releaseDateSelection;

    private ArrayList<Genre> genreArray;
    private ArrayList<String> genreNames;
    private ArrayList<Integer> genreId;
    private String[] genreList;
    private boolean[] isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ratingTextView = findViewById(R.id.ratingTextView);
        ratingSeekBar = findViewById(R.id.ratingSeekBar);
        ratingResultTextView = findViewById(R.id.ratingResultTextView);
        numberOfRatingsTextView = findViewById(R.id.numberOfRatingsTextView);
        numberOfRatingsSeekBar = findViewById(R.id.numberOfRatingsSeekBar);
        numberOfRatingsResultTextView = findViewById(R.id.numberOfRatingsResultTextView);
        releaseDateTextView = findViewById(R.id.releaseDateTextView);
        releaseDateSeekBar = findViewById(R.id.releaseDateSeekBar);
        releaseDateResultTextView = findViewById(R.id.releaseDateResultTextView);
        moviesCheckBox = findViewById(R.id.moviesCheckBox);
        tvShowsCheckBox = findViewById(R.id.tvShowsCheckBox);
        // genreButton = findViewById(R.id.genreButton);
        searchButton = findViewById(R.id.searchButton);

        ratingResultTextView.setText(0.0 + " out of 10.0");
        numberOfRatingsResultTextView.setText(0 + " ratings or more");
        releaseDateResultTextView.setText(1970 + "");

        // Initialize genre list
        genreArray = new ArrayList<>();
        genreArray.add(new Genre("Action", 28, false));
        genreArray.add(new Genre("Adventure", 12, false));
        genreArray.add(new Genre("Animation", 16, false));
        genreArray.add(new Genre("Comedy", 35, false));
        genreArray.add(new Genre("Crime", 80, false));
        genreArray.add(new Genre("Documentary", 99, false));
        genreArray.add(new Genre("Drama", 18, false));
        genreArray.add(new Genre("Family", 10751, false));
        genreArray.add(new Genre("Fantasy", 14, false));
        genreArray.add(new Genre("History", 36, false));
        genreArray.add(new Genre("Horror", 27, false));
        genreArray.add(new Genre("Music", 10402, false));
        genreArray.add(new Genre("Mystery", 9648, false));
        genreArray.add(new Genre("Romance", 10749, false));
        genreArray.add(new Genre("Science Fiction", 878, false));
        genreArray.add(new Genre("TV Movie", 10770, false));
        genreArray.add(new Genre("Thriller", 53, false));
        genreArray.add(new Genre("War", 10752, false));
        genreArray.add(new Genre("Western", 37, false));

        genreNames = new ArrayList<>();
        genreId = new ArrayList<>();

        genreList = new String[genreArray.size()];
        isChecked = new boolean[genreArray.size()];

        for (int i = 0; i < genreArray.size(); i++) {
            genreList[i] = genreArray.get(i).getName();
            isChecked[i] = genreArray.get(i).isChecked();
        }

        moviesCheckBox.setChecked(false);
        tvShowsCheckBox.setChecked(false);
        isMovieChecked = false;
        isTvShowChecked = false;
        df = new DecimalFormat("#0.0");

        ratingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ratingSelection = progress * 0.1f;
                ratingResultTextView.setText(df.format(ratingSelection) + " out of " + ratingSeekBar.getMax() * 0.1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ratingSelection = Float.parseFloat(df.format(ratingSelection));
                Log.d(TAG, ratingSelection + "");
            }
        });

        numberOfRatingsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                numberOfRatingsSelection = progress * 200;
                numberOfRatingsResultTextView.setText(numberOfRatingsSelection + " ratings or more");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        releaseDateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                releaseDateSelection = 1970 + progress;
                releaseDateResultTextView.setText(releaseDateSelection + " ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moviesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isMovieChecked = true;
                    Log.d(TAG, "Checked");
                } else {
                    isMovieChecked = false;
                    Log.d(TAG, "Not checked");
                }
            }
        });

        tvShowsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isTvShowChecked = true;
                } else {
                    isTvShowChecked = false;
                }
            }
        });

        /*
        genreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                // View inflatedView = inflater.inflate(R.layout.movie_list_item, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select Genres");

                builder.setMultiChoiceItems(genreList, isChecked, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    genreId.add(genreArray.get(which).getGenreId());
                                    genreNames.add(genreArray.get(which).getName());
                                }
                            }
                        });

                // builder.setView(inflatedView);
                builder.setPositiveButton("OK", null);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < isChecked.length; i++) {
                            isChecked[i] = false;
                        }
                    }
                });
                builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < isChecked.length; i++) {
                            isChecked[i] = true;
                        }
                    }
                });
                // AlertDialog dialog = builder.create();
                builder.show();
            }
        });
        */

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMovieChecked || isTvShowChecked) {
                    Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                    intent.putExtra("rating", ratingSelection);
                    intent.putExtra("number_of_ratings", numberOfRatingsSelection);
                    intent.putExtra("release_date", releaseDateSelection);
                    intent.putExtra("movies", isMovieChecked);
                    intent.putExtra("tv_shows", isTvShowChecked);
                    // intent.putIntegerArrayListExtra("genre_id", genreId);
                    // intent.putStringArrayListExtra("genre_to_search", genreNames);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Please select Movies or Tv Shows", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
