package com.tou4u.sentour.contentviewer;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.contentviewer.main.MainActivity;
import com.tou4u.sentour.downloading.DetailDownloader;
import com.tou4u.sentour.downloading.PhotoDownloader;


public class ContentLoadManager implements ContentAdapter.BindCallBack {

    private static final String TAG = "ContentLoadManager";

    private MainActivity mActivity;

    private PhotoDownloader<ContentHolder> mPhotoDownloader;
    private DetailDownloader<ContentHolder> mDetailDownloader;

    public ContentLoadManager(MainActivity activity) {
        mActivity = activity;
        try {

            Handler responseHandlerForPhoto = new Handler();
            mPhotoDownloader = new PhotoDownloader<>(responseHandlerForPhoto);

            mPhotoDownloader.setPhotoDownloadListener(
                    new PhotoDownloader.PhotoDownloadListener<ContentHolder>() {
                        @Override
                        public void onPhotoDownloaded(ContentHolder contentHolder, Bitmap bitmap) {
                            try {
                                if (bitmap != null) {
                                    Drawable drawable = new BitmapDrawable(mActivity.getResources(), bitmap);
                                    contentHolder.bindPhoto(drawable);
                                }
                            } catch (IllegalStateException e) {
                                DebugUtil.logE(TAG, "Error bind image", e);
                            }
                        }
                    }
            );

            mPhotoDownloader.start();
            mPhotoDownloader.getLooper();

            Handler responseHandlerForDetail = new Handler();
            mDetailDownloader = new DetailDownloader<>(responseHandlerForDetail);

            mDetailDownloader.setDetailDownloadListener(
                    new DetailDownloader.DetailDownloadListener<ContentHolder>() {
                        @Override
                        public void onDetailDownloaded(ContentHolder contentHolder, String detail) {
                            try {
                                if (detail != null) {
                                    contentHolder.bindDetail(detail);
                                }
                            } catch (IllegalStateException e) {
                                DebugUtil.logE(TAG, "Error bind detail", e);
                            }
                        }
                    }
            );

            mDetailDownloader.start();
            mDetailDownloader.getLooper();
        } catch (IllegalStateException e) {
            DebugUtil.logE(TAG, "Error On Create", e);
        }
    }

    public void clearQueue() {
        if (mPhotoDownloader != null)
            mPhotoDownloader.clearQueue();
        if (mDetailDownloader != null)
            mDetailDownloader.clearQueue();
    }

    public void quit() {
        if (mPhotoDownloader != null)
            mPhotoDownloader.quit();
        if (mDetailDownloader != null)
            mDetailDownloader.quit();
    }

    @Override
    public void onBind(ContentHolder holder, String url, String contentID) {
        if (mPhotoDownloader != null) {
            mPhotoDownloader.queuePhoto(holder, url);
        }
        if (mDetailDownloader != null) {
            mDetailDownloader.queueDetail(holder, contentID);
        }
    }
}
