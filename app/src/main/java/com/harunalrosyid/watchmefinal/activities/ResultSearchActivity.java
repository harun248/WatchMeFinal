package com.harunalrosyid.watchmefinal.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.harunalrosyid.watchmefinal.R;
import com.harunalrosyid.watchmefinal.adapters.MoviesAdapter;
import com.harunalrosyid.watchmefinal.adapters.TvShowAdapter;
import com.harunalrosyid.watchmefinal.entities.movie.Movie;
import com.harunalrosyid.watchmefinal.entities.movie.MovieGenre;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShow;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShowGenre;
import com.harunalrosyid.watchmefinal.models.MovieModel;
import com.harunalrosyid.watchmefinal.models.TvShowModel;
import com.stone.vega.library.VegaLayoutManager;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultSearchActivity extends AppCompatActivity {

    static final String KEYWORD = "keyword";
    static final String INDEX = "index";
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.null_result)
    LinearLayout nullresult;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.txtResult)
    TextView txtResult;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    private String QUERY;
    private String LANGUAGE;
    private MoviesAdapter moviesAdapter;
    private final Observer<ArrayList<Movie>> getMovies = new Observer<ArrayList<Movie>>() {
        @Override
        public void onChanged(ArrayList<Movie> items) {
            if (items != null) {
                moviesAdapter.addMovies(items);
                showLoading();
                if (moviesAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };

    private final Observer<ArrayList<MovieGenre>> getMovieGenres = new Observer<ArrayList<MovieGenre>>() {
        @Override
        public void onChanged(ArrayList<MovieGenre> items) {
            if (items != null) {
                moviesAdapter.addGenres(items);
                showLoading();
            }
        }
    };

    private MovieModel movieModel;
    private TvShowAdapter tvShowAdapter;
    private final Observer<ArrayList<TvShow>> getTvs = new Observer<ArrayList<TvShow>>() {
        @Override
        public void onChanged(ArrayList<TvShow> items) {
            if (items != null) {
                showLoading();
                tvShowAdapter.addTvs(items);
                if (tvShowAdapter.getItemCount() == 0) {
                    showNull();
                    rvSearch.setVisibility(View.GONE);
                }
            } else {
                showLoading();
                showNull();
            }
        }
    };

    private final Observer<ArrayList<TvShowGenre>> getTvGenres = new Observer<ArrayList<TvShowGenre>>() {
        @Override
        public void onChanged(ArrayList<TvShowGenre> items) {
            if (items != null) {
                tvShowAdapter.addGenres(items);
                showLoading();
            } else {
                showLoading();
                showNull();
            }
        }
    };
    private TvShowModel tvShowModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        QUERY = getIntent().getStringExtra(KEYWORD);
        String CATEGORY = getIntent().getStringExtra(INDEX);
        checkConnection();
        getLanguage();


        if (CATEGORY.equals("0")) {
            setupMovieViewModeL();
            setupMovieData();
            setupMovieView();
            Log.i("Categotry", "MOVIE");
        } else {
            setupTvShowModel();
            setupTvData();
            setupTvView();
            Log.i("Categotry", "TV");

        }

    }



    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) Objects.requireNonNull(getApplicationContext())
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading();
            showError();
        }
    }

    private void getLanguage() {
        LANGUAGE = Locale.getDefault().toString();
        if (LANGUAGE.equals("in_ID")) {
            LANGUAGE = "id_ID";
        }
    }

    private void setupTvView() {
        tvShowAdapter = new TvShowAdapter(getApplicationContext(), new ArrayList<>(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), TvShowDetailActivity.class);
            intent.putExtra(TvShowDetailActivity.TID, id);
            startActivity(intent);
        });
        rvSearch.setLayoutManager(new VegaLayoutManager());
        rvSearch.setAdapter(tvShowAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL);
        rvSearch.addItemDecoration(itemDecoration);
    }


    private void setupTvData() {
        tvShowModel.searchTvs(LANGUAGE, QUERY);
        tvShowModel.setGenre(LANGUAGE);
    }

    private void setupTvShowModel() {
        tvShowModel = new ViewModelProvider(this).get(TvShowModel.class);
        tvShowModel.getTvs().observe(this, getTvs);
        tvShowModel.getGenres().observe(this, getTvGenres);

    }

    private void setupMovieView() {
        moviesAdapter = new MoviesAdapter(getApplicationContext(), new ArrayList<>(), new ArrayList<>(), id -> {
            Intent intent = new Intent(getApplicationContext(), MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MID, id);
            startActivity(intent);
        });
        rvSearch.setLayoutManager(new VegaLayoutManager());
        rvSearch.setAdapter(moviesAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(Objects.requireNonNull(getApplicationContext()), DividerItemDecoration.VERTICAL);
        rvSearch.addItemDecoration(itemDecoration);

    }

    private void setupMovieData() {
        movieModel.searchMovies(LANGUAGE, QUERY);
        movieModel.setGenre(LANGUAGE);
    }

    private void setupMovieViewModeL() {
        movieModel = new ViewModelProvider(this).get(MovieModel.class);
        movieModel.getMovies().observe(this, getMovies);
        movieModel.getGenres().observe(this, getMovieGenres);

    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    private void showNull() {
        nullresult.setVisibility(View.VISIBLE);
        txtResult.setText(("\""+QUERY + "\" " + getResources().getString(R.string.null_result)));
    }

    private void showLoading() {
        if (false) {
            avi.show();
        } else {
            avi.hide();
        }
    }
}
