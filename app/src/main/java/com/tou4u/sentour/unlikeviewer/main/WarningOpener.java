package com.tou4u.sentour.unlikeviewer.main;

import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;

import com.tou4u.sentour.R;

public class WarningOpener {

    private static final String TITLE = "**주의**";

    private AlertDialog mWarningDialog;
    private Context mContext;
    private CountDownTimer mTimer;

    public WarningOpener(Context context, CountDownTimer timer) {
        mContext = context;
        mTimer = timer;
    }

    public void dismissDialog() {
        if (mWarningDialog != null && mWarningDialog.isShowing()) {
            mWarningDialog.dismiss();
            mWarningDialog = null;
        }
    }

    public void showDialog() {
        mWarningDialog = new AlertDialog.Builder(mContext)
                .setTitle(TITLE)
                .setMessage(R.string.warning_unlike)
                .setCancelable(false)
                .setPositiveButton("확인하였습니다.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTimer.start();
                    }
                }).show();
    }


}
