package com.tou4u.sentour.contentviewer;

import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.data.Content;
import com.tou4u.sentour.downloading.ContentDownloader;

import java.util.ArrayList;

public class ContentsLoadManager implements ContentDownloader.OnLoadedCallBack{

    private static final String TAG = "ContentsLoadManager";

    private ContentDownloader mContentDownloader;
    private ContentAdapter mContentAdapter;

    public ContentsLoadManager(ContentAdapter adapter){
        mContentAdapter = adapter;
    }

    public void loadContents(String lat, String lon, String sky, Integer contentType, String hashTag){
        if (mContentDownloader != null) {
            mContentDownloader.cancel(true);
        }
        mContentDownloader = new ContentDownloader(this);
        mContentDownloader.load(lat, lon, sky, contentType, hashTag);
    }

    public void cancle(){
        if (mContentDownloader != null)
            mContentDownloader.cancel(true);
    }

    @Override
    public void onLoaded(ArrayList<Content> contents) {
        if(contents != null)
            mContentAdapter.changeContents(contents);
        else
            DebugUtil.logD(TAG,"contents load none");
    }
}
