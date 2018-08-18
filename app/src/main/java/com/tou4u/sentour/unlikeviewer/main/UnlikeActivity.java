package com.tou4u.sentour.unlikeviewer.main;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tou4u.sentour.R;
import com.tou4u.sentour.httpapi.UnlikePostAPI;
import com.tou4u.sentour.util.DebugUtil;

public class UnlikeActivity extends AppCompatActivity {

    public static final String EXTRA_CONTENT_ID = "EXTRA_CONTENT_ID";
    public static final String EXTRA_SKY = "EXTRA_SKY";
    public static final String EXTRA_HASH_TAG = "EXTRA_HASH_TAG";
    public static final String EXTRA_ID = "EXTRA_ID";

    private static final int MAX_TIME = 6;
    private static final String TAG = "UnlikeActivity";


    private TextView mRemainTextView;
    private TextView mTouchTextView;
    private int remain;
    private int touch;

    private volatile boolean flag;

    private int mNotiID;
    private String mContentID;
    private String mSky;
    private String mHashTag;

    private UnlikePostAPI mUnlikePostAPI;
    private CountDownTimer mCountDownTimer;
    private AlertDialog mCloseDialog;
    private WarningOpener mWarningOpener;

    @Override
    protected void onDestroy() {

        if (mCloseDialog != null && mCloseDialog.isShowing()) {
            mCloseDialog.dismiss();
            mCloseDialog = null;
        }

        mWarningOpener.dismissDialog();

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlike);

        Intent i = getIntent();
        mCloseDialog = null;
        mContentID = i.getStringExtra(EXTRA_CONTENT_ID);
        mSky = i.getStringExtra(EXTRA_SKY);
        mHashTag = i.getStringExtra(EXTRA_HASH_TAG);
        mNotiID = i.getIntExtra(EXTRA_ID, 0);

        DebugUtil.logD(TAG, String.format("GET EXTRA ( %s, %s, %s )", mContentID, mSky, mHashTag));

        NotificationManager notiManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notiManager.cancel(mNotiID);

        remain = MAX_TIME;
        touch = 0;
        flag = true;

        mUnlikePostAPI = new UnlikePostAPI();

        mTouchTextView = (TextView) findViewById(R.id.textview_unlike_touch);

        View v = findViewById(R.id.unlike_parent);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    touch++;
                    mTouchTextView.setText("" + touch);
                }

            }
        });

        mRemainTextView = (TextView) findViewById(R.id.textview_unlike_remain);
        mRemainTextView.setText(String.format("%d초 동안", MAX_TIME));

        mCountDownTimer = new CountDownTimer(10 * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(remain > 0){
                    remain--;
                    mRemainTextView.setText(String.format("%d초 동안", remain));
                }
                if (remain == 0) {
                    if (flag) {
                        flag = false;
                        int unlike = touch;
                        if (unlike == 0)
                            unlike = 1;
                        else if (unlike >= 43) {
                            unlike = 46;
                        }
                        mUnlikePostAPI.postUnlike(mContentID, mSky, mHashTag, String.valueOf(unlike));

                        DebugUtil.logD(TAG, "POST IN TIMER");

                        cancel();

                        mCloseDialog = new AlertDialog.Builder(UnlikeActivity.this)
                                .setTitle("감사합니다")
                                .setMessage("평가에 반영되었습니다.")
                                .setCancelable(false)
                                .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DebugUtil.logD(TAG, "FINISH IN TIMER");
                                        UnlikeActivity.this.finish();
                                    }
                                }).show();
                    }
                }
            }

            public void onFinish() {

            }
        };

        mWarningOpener = new WarningOpener(this,mCountDownTimer);
        mWarningOpener.showDialog();


    }

    @Override
    public void onBackPressed() {
        mCountDownTimer.cancel();

        if (flag) {
            flag = false;
            int unlike = touch;
            if (unlike == 0)
                unlike = 1;
            else if (unlike >= 43) {
                unlike = 46;
            }
            mUnlikePostAPI.postUnlike(mContentID, mSky, mHashTag, String.valueOf(unlike));

            DebugUtil.logD(TAG, "POST IN BACK PRESS");

            mCloseDialog = new AlertDialog.Builder(UnlikeActivity.this)
                    .setTitle("감사합니다")
                    .setMessage("평가에 반영되었습니다.")
                    .setCancelable(false)
                    .setPositiveButton("종료", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DebugUtil.logD(TAG, "FINISH IN BACK PRESS");
                            UnlikeActivity.this.finish();
                        }
                    }).show();
        }
    }
}
