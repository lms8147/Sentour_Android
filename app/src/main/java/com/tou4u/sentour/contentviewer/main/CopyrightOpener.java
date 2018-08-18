package com.tou4u.sentour.contentviewer.main;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tou4u.sentour.R;

public class CopyrightOpener {

    private AlertDialog mHashTagDialog;
    private Context mContext;

    private static final String TITLE = "COPYRIGHT";

    private int selectedIndex;

    public CopyrightOpener(Context context) {
        mContext = context;
    }

    public void dismissDialog() {
        if (mHashTagDialog != null && mHashTagDialog.isShowing()) {
            mHashTagDialog.dismiss();
            mHashTagDialog = null;
        }
    }

    public void showDialog() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_copyright, null);

        mHashTagDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setView(v)
                .show();
    }


}
