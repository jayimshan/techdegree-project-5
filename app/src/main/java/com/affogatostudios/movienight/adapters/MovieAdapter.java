package com.affogatostudios.movienight.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.affogatostudios.movienight.R;
import com.affogatostudios.movienight.databinding.MovieListItemBinding;
import com.affogatostudios.movienight.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context) {
        this.movies = new ArrayList<>();
        this.context = context;
    }

    public MovieAdapter(List<Movie> movies,  Context context) {
        this.movies = movies;
        this.context = context;
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        Log.d(TAG, this.movies.size() + "");
    }

    public void clearMovies() {
        this.movies.clear();
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
        /*
        MovieListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.movie_list_item, viewGroup, false);
        return new ViewHolder(binding);
        */
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Movie movie = movies.get(i);
        // viewHolder.movieListItemBinding.setMovie(movie);
        viewHolder.titleView.setText(movie.getTitle());
        viewHolder.genreView.setText("Genre: " + movie.getGenre());
        viewHolder.releaseDateView.setText("Release Date: " + movie.getReleaseDate());
        viewHolder.ratingView.setText("Rating: " + movie.getRating());
        viewHolder.voteCountView.setText("Vote Count: " + movie.getNumberOfRatings());
        if (movie.getImagePath().equals("https://image.tmdb.org/t/p/w300null")) {
            Log.d(TAG, movie.getImagePath());
            viewHolder.imageView.setImageResource(R.drawable.no_image_available);
        } else {
            Log.d(TAG, movie.getImagePath());
            Picasso.get().load(movie.getImagePath()).into(viewHolder.imageView);
        }
        // final ImageView image = new ImageView(context);
        // Picasso.get().load(movie.getImagePath2()).into(image);
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(movie.getTitle());
                builder.setMessage(movie.getOverview());
                // builder.setView(image);
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Binding variables
        public MovieListItemBinding movieListItemBinding;
        TextView titleView;
        TextView genreView;
        TextView releaseDateView;
        TextView ratingView;
        TextView voteCountView;
        ImageView imageView;
        RelativeLayout parentLayout;

        // Constructor to do view lookups for each subview
        /*
        public ViewHolder(MovieListItemBinding movieLayoutBinding) {
            super(movieLayoutBinding.getRoot());
            movieListItemBinding = movieLayoutBinding;
        }
        */

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textView);
            genreView = itemView.findViewById(R.id.textView2);
            releaseDateView = itemView.findViewById(R.id.textView3);
            ratingView = itemView.findViewById(R.id.textView4);
            voteCountView = itemView.findViewById(R.id.textView5);
            imageView = itemView.findViewById(R.id.imageView);
            parentLayout = itemView.findViewById(R.id.movieListView);
        }
    }
}
