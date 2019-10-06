package com.harunalrosyid.watchmefinal.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.harunalrosyid.watchmefinal.R;
import com.harunalrosyid.watchmefinal.databases.FavoriteHelper;
import com.harunalrosyid.watchmefinal.entities.favorite.Favorite;
import com.harunalrosyid.watchmefinal.entities.movie.Movie;
import com.harunalrosyid.watchmefinal.models.MovieModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.BACKDROP;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.CATEGORY;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.ID;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.POSTER;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.RATING;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.harunalrosyid.watchmefinal.databases.DatabaseContract.FavoriteColumns.TITLE;
import static com.harunalrosyid.watchmefinal.api.ApiUtils.IMAGE_URL;

@SuppressWarnings("ALL")
public class MovieDetailActivity extends AppCompatActivity {
    public static final String MID = "movie_id";
    public static final String GENRE = "genre";
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.imgBackdrop)
    ImageView imgBackdrop;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.txtReleaseDate)
    TextView txtReleaseDate;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtRating)
    TextView txtRating;
    @BindView(R.id.labelOverview)
    TextView labelOverview;
    @BindView(R.id.txtOverview)
    TextView txtOverview;
    @BindView(R.id.image_favorite)
    ImageView imgFavorite;
    private MovieModel movieModel;
    private int MOVIE_ID;
    private String MOVIE_GENRE;
    private int id, mId;
    private FavoriteHelper helper;
    private String backdropPath, title, releaseDate, overview, poster, rating, category;
    private final Observer<Movie> getMovie = new Observer<Movie>() {
        @Override
        public void onChanged(Movie movie) {
            if (movie != null) {
                showLoading();
                imgFavorite.setVisibility(View.VISIBLE);
                imgBackdrop.setVisibility(View.VISIBLE);
                Glide.with(MovieDetailActivity.this)
                        .load(IMAGE_URL + movie.getBackdropPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgBackdrop);

                imgPoster.setVisibility(View.VISIBLE);
                Glide.with(MovieDetailActivity.this)
                        .load(IMAGE_URL + movie.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgPoster);
                txtTitle.setText(movie.getTitle());
                txtReleaseDate.setVisibility(View.VISIBLE);
                txtReleaseDate.setText(movie.getReleaseDate());
                txtRating.setText(movie.getRating().toString());
                labelOverview.setVisibility(View.VISIBLE);

                mId = movie.getId();
                title = movie.getTitle();
                poster = movie.getPosterPath();
                backdropPath = movie.getBackdropPath();
                releaseDate = movie.getReleaseDate();
                rating = movie.getRating().toString();
                overview = movie.getOverview();
                checkFavorite();
                if (movie.getOverview().length() == 0) {
                    txtOverview.setText(getResources().getString(R.string.not_found));
                } else {
                    txtOverview.setText(movie.getOverview());
                }
            }
        }
    };
    private Favorite favorite = new Favorite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        checkConnection();
        setupViewModeL();
        setupData();

    }


    private void checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
        } else {
            showLoading();
            showError();
        }
    }

    private void setupData() {
        helper = new FavoriteHelper(getApplicationContext());
        helper.open();
        MOVIE_ID = getIntent().getIntExtra(MID, MOVIE_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        movieModel.setMovie(MOVIE_ID, LANGUANGE);

    }

    private void setupViewModeL() {
        movieModel = ViewModelProviders.of(this).get(MovieModel.class);
        movieModel.getMovie().observe(this, getMovie);

    }

    private void showLoading() {
        if (false) {
            avi.smoothToShow();
        } else {
            avi.smoothToHide();
        }
    }


    private void showError() {
        errorLayout.setVisibility(View.VISIBLE);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    @OnClick({R.id.image_favorite})
    public void doFavorite(View view) {
        if (checkFavorite()) {
            Uri uri = Uri.parse(CONTENT_URI + "/" + id);
            int i = getContentResolver().delete(uri, null, null);
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
            Toast.makeText(this, getString(R.string.unfavorite), Toast.LENGTH_SHORT).show();

        } else {
            favorite.setmId(mId);
            favorite.setTitle(title);
            favorite.setPoster(poster);
            favorite.setBackdrop(backdropPath);
            favorite.setRating(rating);
            favorite.setReleaseDate(releaseDate);
            favorite.setOverview(overview);
            favorite.setCategoty("movie");

            ContentValues values = new ContentValues();
            values.put(ID, mId);
            values.put(TITLE, title);
            values.put(POSTER, poster);
            values.put(BACKDROP, backdropPath);
            values.put(RATING, rating);
            values.put(RELEASE_DATE, releaseDate);
            values.put(OVERVIEW, overview);
            values.put(CATEGORY, "movie");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                imgFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                Toast.makeText(this, title + " " + getString(R.string.favorite_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkFavorite() {
        Uri uri = Uri.parse(CONTENT_URI + "");
        boolean favorite = false;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int getmId;
        if (cursor.moveToFirst()) {
            do {
                getmId = cursor.getInt(1);
                if (getmId == mId) {
                    id = cursor.getInt(0);
                    imgFavorite.setImageResource(R.drawable.ic_favorite);
                    favorite = true;
                }
            } while (cursor.moveToNext());

        }

        return favorite;
    }

}
