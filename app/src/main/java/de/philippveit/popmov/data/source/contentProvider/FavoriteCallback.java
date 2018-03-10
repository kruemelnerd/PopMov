package de.philippveit.popmov.data.source.contentProvider;

import android.net.Uri;

public interface FavoriteCallback {
    void onResponseInsert(Uri uri);

    void onResponseDelete(int amount);

    void onFailure();
}
