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
    List<String> mList;
    private final List<String> arrayList = new ArrayList<>();
    Cursor audioCursor;

    public AudioAsyncLoader(Context context) {
        super(context);
    }


    @Override
    public List<String> loadInBackground() {
        ContentResolver audioResolver = getContext().getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        audioCursor = audioResolver.query(audioUri, null, null, null, null);
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

    @Override
    public void deliverResult(List<String> data) {
        if(isReset()){
            if(data != null){
                releaseResources(data);
            }
        }
        List<String> oldList = mList;
        mList = data;
        if(isStarted()){
            super.deliverResult(data);
        }
        if(oldList != null){
            releaseResources(oldList);
        }
    }

    @Override
    protected void onStartLoading() {
        if(mList != null){
            deliverResult(mList);
        }
        if(takeContentChanged() || mList == null){
            forceLoad();
        }
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void onCanceled(List<String> data) {
        super.onCanceled(data);
        releaseResources(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if(mList != null){
            releaseResources(mList);
            mList = null;
        }
    }

    private void releaseResources(List<String> data) {
        if (data!= null && !data.isEmpty())
            data.clear();

    }
}
