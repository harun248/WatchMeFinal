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
import android.widget.ScrollView;
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
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShow;
import com.harunalrosyid.watchmefinal.models.TvShowModel;
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
import static com.harunalrosyid.watchmefinal.widget.FavoriteWidget.sendRefreshBroadcast;

@SuppressWarnings("ALL")
public class TvShowDetailActivity extends AppCompatActivity {

    public static final String TID = "tv_id";
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.imgBackdrop)
    ImageView imgBackdrop;
    @BindView(R.id.imgPoster)
    ImageView imgPoster;
    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtReleaseDate)
    TextView txtReleaseDate;
    @BindView(R.id.txtRating)
    TextView txtRating;
    @BindView(R.id.labelOverview)
    TextView labelOverview;
    @BindView(R.id.txtOverview)
    TextView txtOverview;
    @BindView(R.id.imgFavorite)
    ImageView imgFavorite;
    @BindView(R.id.content)
    ScrollView content;
    private TvShowModel tvShowModel;
    private int TV_ID;
    private int id, mId;
    private FavoriteHelper helper;
    private String backdropPath, title, releaseDate, overview, poster, rating, category;
    private final Observer<TvShow> getTv = new Observer<TvShow>() {
        @Override
        public void onChanged(TvShow tvShow) {
            if (tvShow != null) {
                showLoading();
                imgFavorite.setVisibility(View.VISIBLE);
                imgBackdrop.setVisibility(View.VISIBLE);
                Glide.with(TvShowDetailActivity.this)
                        .load(IMAGE_URL + tvShow.getBackdropPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgBackdrop);

                imgPoster.setVisibility(View.VISIBLE);
                Glide.with(TvShowDetailActivity.this)
                        .load(IMAGE_URL + tvShow.getPosterPath())
                        .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                        .into(imgPoster);
                txtTitle.setText(tvShow.getOriginalName());
                txtReleaseDate.setVisibility(View.VISIBLE);
                txtReleaseDate.setText(tvShow.getFirstAirDate());
                txtRating.setText(tvShow.getRating().toString());
                labelOverview.setVisibility(View.VISIBLE);

                mId = tvShow.getId();
                title = tvShow.getOriginalName();
                poster = tvShow.getPosterPath();
                backdropPath = tvShow.getBackdropPath();
                releaseDate = tvShow.getFirstAirDate();
                rating = tvShow.getRating().toString();
                overview = tvShow.getOverview();
                checkFavorite();
                if (tvShow.getOverview().length() == 0) {
                    txtOverview.setText(getResources().getString(R.string.not_found));

                } else {
                    txtOverview.setText(tvShow.getOverview());

                }
            }
        }
    };
    private Favorite favorite = new Favorite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tvshow_detail);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ButterKnife.bind(this);
        checkConnection();
        setupViewModeL();
        setupData();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
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
        TV_ID = getIntent().getIntExtra(TID, TV_ID);
        String LANGUANGE = Locale.getDefault().toString();
        if (LANGUANGE.equals("in_ID")) {
            LANGUANGE = "id_ID";
        }
        tvShowModel.setTv(TV_ID, LANGUANGE);

    }

    private void setupViewModeL() {

        tvShowModel = ViewModelProviders.of(this).get(TvShowModel.class);
        tvShowModel.getTv().observe(this, getTv);

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


    @OnClick({R.id.imgFavorite})
    public void doFavorite(View view) {
        if (checkFavorite()) {
            Uri uri = Uri.parse(CONTENT_URI + "/" + id);
            getContentResolver().delete(uri, null, null);
            imgFavorite.setImageResource(R.drawable.ic_unfavorite);
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
            values.put(CATEGORY, "tv");

            if (getContentResolver().insert(CONTENT_URI, values) != null) {
                Toast.makeText(this, title + " " + getString(R.string.favorite), Toast.LENGTH_SHORT).show();
                imgFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                Toast.makeText(this, title + " " + getString(R.string.favorite_error), Toast.LENGTH_SHORT).show();
            }

        }
        sendRefreshBroadcast(getApplicationContext());

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
