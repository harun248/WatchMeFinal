package com.harunalrosyid.watchmefinal.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.harunalrosyid.watchmefinal.R;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShow;
import com.harunalrosyid.watchmefinal.entities.tvshow.TvShowGenre;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.harunalrosyid.watchmefinal.api.ApiUtils.IMAGE_URL;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {


    private final Context mContext;
    private final PostItemListener mItemListener;
    private List<TvShow> mItems;
    private List<TvShowGenre> mTvShowGenre;

    public TvShowAdapter(Context context, List<TvShow> posts, List<TvShowGenre> genres, PostItemListener itemListener) {
        mTvShowGenre = genres;
        mItems = posts;
        mContext = context;
        mItemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TvShow tvs = mItems.get(position);
        holder.itemMovieReleaseDate.setText(tvs.getFirstAirDate().split("-")[0]);
        if (tvs.getOriginalName().length() > 15) {
            holder.itemMovieTitle.setText((tvs.getOriginalName().substring(0, 15) + "..."));
        } else {
            holder.itemMovieTitle.setText(tvs.getOriginalName());
        }
        holder.itemMovieRating.setText(String.valueOf(tvs.getRating()));
        holder.itemMovieGenre.setText(holder.getTvGenres(tvs.getGenreIds()));
        Glide.with(mContext)
                .load(IMAGE_URL + tvs.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(holder.itemMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addTvs(List<TvShow> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public void addGenres(List<TvShowGenre> items) {
        mTvShowGenre = items;
        notifyDataSetChanged();
    }

    private TvShow getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(int id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final PostItemListener mItemListener;
        @BindView(R.id.item_movie_poster)
        ImageView itemMoviePoster;
        @BindView(R.id.item_movie_release_date)
        TextView itemMovieReleaseDate;
        @BindView(R.id.item_movie_title)
        TextView itemMovieTitle;
        @BindView(R.id.item_movie_genre)
        TextView itemMovieGenre;
        @BindView(R.id.item_movie_rating)
        TextView itemMovieRating;
        @BindView(R.id.btn_movie)
        CardView btnMovie;

        ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        private String getTvGenres(List<Long> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Long genreId : genreIds) {
                for (TvShowGenre genre : mTvShowGenre) {
                    if (genre.getId().equals(genreId)) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }

        @Override
        public void onClick(View view) {
            TvShow item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item.getId());
            notifyDataSetChanged();
        }
    }
}
