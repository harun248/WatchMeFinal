package com.harunalrosyid.watchmefinal.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.harunalrosyid.watchmefinal.R;
import com.harunalrosyid.watchmefinal.activities.MovieDetailActivity;
import com.harunalrosyid.watchmefinal.adapters.MoviesAdapter;
import com.harunalrosyid.watchmefinal.entities.movie.Movie;
import com.harunalrosyid.watchmefinal.entities.movie.MovieGenre;
import com.harunalrosyid.watchmefinal.models.MovieModel;
import com.stone.vega.library.VegaLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    private MoviesAdapter mAdapter;
    private final Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> items) {
            if (items != null) {
                mAdapter.addMovies(items);
                showLoading(false);
            }
        }
    };
    private final Observer<ArrayList<MovieGenre>> getGenres = new Observer<ArrayList<MovieGenre>>() {
        @Override
        public void onChanged(ArrayList<MovieGenre> items) {
            if (items != null) {
                mAdapter.addGenres(items);
                showLoading(false);
            }
        }
    };
    private MovieModel movieModel;

    public MovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.movie_fragment, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkConnection();
        setupViewModeL();
        setupData();
        setupView();

    }

    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) Objects.requireNonNull(getContext())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading(false);
            showError();
        }
    }

    private void setupView() {
        mAdapter = new MoviesAdapter(getContext(), new ArrayList<>(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MID, id);
            startActivity(intent);
        });
        rvMovie.setLayoutManager(new VegaLayoutManager());
        rvMovie.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getContext()), DividerItemDecoration.VERTICAL);
        rvMovie.addItemDecoration(itemDecoration);
    }

    private void setupData() {
        String LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
        movieModel.setMovies(LANGUAGE);
        movieModel.setGenre(LANGUAGE);
    }

    private void setupViewModeL() {
        movieModel = new ViewModelProvider(this).get(MovieModel.class);
        movieModel.getMovies().observe(this, getMovies);
        movieModel.getGenres().observe(this, getGenres);

    }

    private void showLoading(Boolean state) {
        if (state) {
            avi.show();
        } else {
            avi.hide();
        }
    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }


}
