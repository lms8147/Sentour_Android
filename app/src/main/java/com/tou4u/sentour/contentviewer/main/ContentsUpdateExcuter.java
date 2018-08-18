package com.tou4u.sentour.contentviewer.main;

import android.content.Context;
import android.widget.Toast;

import com.tou4u.sentour.contentviewer.main.network.NetworkUtil;
import com.tou4u.sentour.util.DebugUtil;

public class ContentsUpdateExcuter implements MainDataObserver {

    private static final String TAG = "ContentsUpdateExcuter";

    private volatile boolean flag;

    private OnUpdateCallBack mCallBack;

    private Context mContext;

    interface OnUpdateCallBack {
        void onContentsUpdate(String lat, String lng, String hashTag, String sky, int currentPage);
    }

    @Override
    public void update(String lat, String lng, String hashTag, String sky, int currentPage) {
        if (NetworkUtil.getInstance().isConnected(mContext) == NetworkUtil.CODE_CONNECT) {
            DebugUtil.logD(TAG, String.format("ContentsUpdateExcute %s, %s, %s, %s, %d", lat, lng, hashTag, sky, currentPage));
            mCallBack.onContentsUpdate(lat, lng, hashTag, sky, currentPage);
        } else {
            Toast.makeText(mContext, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public ContentsUpdateExcuter(Context context, OnUpdateCallBack callBack, MainDataSubject subject) {
        mCallBack = callBack;
        mContext = context;
        subject.addObserver(this);
    }

}
