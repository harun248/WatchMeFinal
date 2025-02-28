package com.harunalrosyid.watchmefinal.databases;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.harunalrosyid.watchmefinal";
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }


    public static final class FavoriteColumns implements BaseColumns {
        public static final String TABLE_FAVORITE = "favorites_db";
        public static final String ID = "mId";
        public static final String TITLE = "title";
        public static final String POSTER = "poster";
        public static final String BACKDROP = "backdrop";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release_date";
        public static final String OVERVIEW = "overview";
        public static final String CATEGORY = "category";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE)
                .build();

    }

}
