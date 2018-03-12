package de.philippveit.popmov.data.source.contentProvider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "favorite_movies.db";

    private static final int DATABASE_VERSION = 3;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE =
                "CREATE TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " (" +
                        FavoriteContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteContract.FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                        FavoriteContract.FavoriteEntry.COLUMN_JSON + " TEXT NOT NULL);";
        // No other Column to keep it simple
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < 3){
            db.execSQL("ALTER TABLE " + FavoriteContract.FavoriteEntry.TABLE_NAME + " ADD COLUMN " + FavoriteContract.FavoriteEntry.COLUMN_JSON + " TEXT NOT NULL;");
        }
        onCreate(db);
    }
}
