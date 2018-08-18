package com.tou4u.sentour.contentviewer.main;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.tou4u.sentour.R;

public class InfoOpener {

    private AlertDialog mHashTagDialog;
    private Context mContext;

    private static final String TITLE = "INFO";

    public InfoOpener(Context context) {
        mContext = context;
    }

    public void dismissDialog() {
        if (mHashTagDialog != null && mHashTagDialog.isShowing()) {
            mHashTagDialog.dismiss();
            mHashTagDialog = null;
        }
    }

    public void showDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_info, null);

        mHashTagDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setView(v)
                .show();
    }


}
