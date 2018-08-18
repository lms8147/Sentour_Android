package com.tou4u.sentour.contentviewer.main;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

import com.tou4u.sentour.R;

public class WarningOpener {

    private static final String TITLE = "**주의**";

    private AlertDialog mWarningDialog;
    private Context mContext;

    public WarningOpener(Context context) {
        mContext = context;
    }

    public void dismissDialog(){
        if (mWarningDialog != null && mWarningDialog.isShowing()) {
            mWarningDialog.dismiss();
            mWarningDialog = null;
        }
    }

    public void showDialog() {
        mWarningDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setMessage(R.string.warning_main)
                .setCancelable(false)
                .setPositiveButton("확인하였습니다.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }


}
