package com.harunalrosyid.favoriteme.fargments;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.harunalrosyid.favoriteme.R;
import com.harunalrosyid.favoriteme.adapters.FavoriteTvShowAdapter;
import com.harunalrosyid.favoriteme.entities.Favorite;
import com.harunalrosyid.favoriteme.activities.TvShowDetailActivity;
import com.stone.vega.library.VegaLayoutManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.harunalrosyid.favoriteme.database.DatabaseContract.FavoriteColumns.CONTENT_URI;
import static com.harunalrosyid.favoriteme.helper.MappingHelper.getTvFavoriteList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowFragment extends Fragment {


    private static final String EXTRA_STATE = "EXTRA_STATE";
    @BindView(R.id.null_layout)
    LinearLayout nullLayout;
    @BindView(R.id.rv_tv)
    RecyclerView rvTv;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    private FavoriteTvShowAdapter adapter;

    public FavoriteTvShowFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fav_tvshow_fragment, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    private void setupView() {
        LoadFavoriteCallback callback = new LoadFavoriteCallback() {
            @Override
            public void postExecute(Cursor cursor) {
                ArrayList<Favorite> list = getTvFavoriteList(cursor);
                if (list.size() > 0) {
                    adapter.setListFavorite(list);
                    nullLayout.setVisibility(View.GONE);
                    rvTv.setVisibility(View.VISIBLE);
                } else {
                    adapter.setListFavorite(new ArrayList<>());
                    nullLayout.setVisibility(View.VISIBLE);
                    rvTv.setVisibility(View.GONE);
                }
            }
        };
        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext(), callback);
        Objects.requireNonNull(getActivity()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        rvTv.setLayoutManager(new VegaLayoutManager());
        rvTv.setHasFixedSize(true);

        adapter = new FavoriteTvShowAdapter(getActivity(), id -> {
            Intent intent = new Intent(getContext(), TvShowDetailActivity.class);
            intent.putExtra(TvShowDetailActivity.TID, id);
            startActivity(intent);
        });
        rvTv.setAdapter(adapter);
        new LoadFavoriteAsync(getContext(), callback).execute();

    }


    interface LoadFavoriteCallback {

        void postExecute(Cursor cursor);
    }

    private static class LoadFavoriteAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoriteCallback> weakCallback;

        private LoadFavoriteAsync(Context context, LoadFavoriteCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;
        final LoadFavoriteCallback callback;

        DataObserver(Handler handler, Context context, LoadFavoriteCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoriteAsync(context, callback).execute();

        }
    }

}
