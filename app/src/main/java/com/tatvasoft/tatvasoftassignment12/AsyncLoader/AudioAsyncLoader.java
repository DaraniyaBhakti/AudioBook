package com.tatvasoft.tatvasoftassignment12.AsyncLoader;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class AudioAsyncLoader extends AsyncTaskLoader<List<String>> {
    private final ArrayList<String> arrayList = new ArrayList<>();

    public AudioAsyncLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public List<String> loadInBackground() {
        ContentResolver audioResolver = getContext().getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor audioCursor = audioResolver.query(audioUri, null, null, null, null);
        if (audioCursor != null && audioCursor.moveToNext()) {
            int titleColumn = audioCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            do {
                String title = audioCursor.getString(titleColumn);
                arrayList.add(title);
            } while (audioCursor.moveToNext());
        }
        assert audioCursor != null;
        audioCursor.close();
        return arrayList;
    }
}
