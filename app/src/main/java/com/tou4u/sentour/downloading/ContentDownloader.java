package com.tou4u.sentour.downloading;

import android.os.AsyncTask;

import com.tou4u.sentour.contentviewer.fragment.ContentListFragment;
import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.data.Content;
import com.tou4u.sentour.httpapi.ContentAPI;

import java.util.ArrayList;

public class ContentDownloader extends AsyncTask<Object, Void, ArrayList<Content>> {

    private static final String TAG = "ContentDownloader";

    private OnLoadedCallBack mOnLoadedCallBack;

    private ContentAPI api;

    public interface OnLoadedCallBack {
        void onLoaded(ArrayList<Content> contents);
    }

    public ContentDownloader(OnLoadedCallBack onLoadedCallBack) {
        mOnLoadedCallBack = onLoadedCallBack;
        api = new ContentAPI();
    }

    @Override
    protected void onPostExecute(ArrayList<Content> contents) {
        super.onPostExecute(contents);
        if (contents != null)
            mOnLoadedCallBack.onLoaded(contents);

    }

    @Override
    protected ArrayList<Content> doInBackground(Object... params) {

        try {
            String lat = (String) params[0];
            String lng = (String) params[1];
            String sky = (String) params[2];
            int contentType = (Integer) params[3];
            String hashTag = (String) params[4];

            DebugUtil.logD(TAG, String.format("%s, %s, %s, %d, %s", lat, lng, sky, contentType, hashTag));

            ArrayList<Content> contents = api.getContents(lat, lng, ContentListFragment.RADIUS, contentType, sky, hashTag);

            return contents;
        } catch (NullPointerException e) {
            DebugUtil.logE(TAG, "Error On Background", e);
        }
        return null;
    }

    public void load(String lat, String lon, String sky, Integer contentType, String hashTag) {
        execute(lat, lon, sky, contentType, hashTag);
    }
}
