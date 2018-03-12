package de.philippveit.popmov.data.source.contentProvider;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String CONTENT_AUTHORITY = "de.philippveit.popmov";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movieid";
        public static final String COLUMN_JSON = "json";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static Uri buildFavoriteUriWithId(long id) {
            return CONTENT_URI
                    .buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }
    }
}
