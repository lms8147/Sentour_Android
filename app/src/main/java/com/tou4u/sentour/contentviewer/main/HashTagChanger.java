package com.tou4u.sentour.contentviewer.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

public class HashTagChanger {

    private static final String[] TOUR =
            new String[]{
                    "#혼자여행",
                    "#가족여행",
                    "#사랑",
                    "#고독"
            };

    private static final String TITLE = "여행 테마에 맞는 해시 태그를 선택해주세요.";

    private AlertDialog mHashTagDialog;
    private MainDataSubject mMainDataSubject;
    private AppCompatTextView mHashTagTextView;
    private Context mContext;

    private int selectedIndex;

    public HashTagChanger(Context context, MainDataSubject mainDataSubject, AppCompatTextView hashTagTextView) {
        mContext = context;
        mMainDataSubject = mainDataSubject;
        mHashTagTextView = hashTagTextView;
        selectedIndex = 0;
    }

    public void dismissDialog(){
        if (mHashTagDialog != null && mHashTagDialog.isShowing()) {
            mHashTagDialog.dismiss();
            mHashTagDialog = null;
        }
    }

    public void showCancelableDialog() {

        final int temp = selectedIndex;

        mHashTagDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setCancelable(false)
                .setSingleChoiceItems(TOUR, selectedIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
                    }
                })
                .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMainDataSubject.setHashTag(TOUR[selectedIndex]);
                        mHashTagTextView.setText(TOUR[selectedIndex]);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = temp;
                    }
                }).show();
    }

    public void showDialog() {
        mHashTagDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setCancelable(false)
                .setSingleChoiceItems(TOUR, selectedIndex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedIndex = which;
                    }
                })
                .setPositiveButton("선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMainDataSubject.setHashTag(TOUR[selectedIndex]);
                        mHashTagTextView.setText(TOUR[selectedIndex]);
                    }
                }).show();
    }


}
