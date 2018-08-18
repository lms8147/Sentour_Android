package com.tou4u.sentour.contentviewer;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.R;
import com.tou4u.sentour.contentviewer.main.MainActivity;
import com.tou4u.sentour.data.Content;

public class ContentHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "ContentHolder";

    private MainActivity mActivity;

    private Content mContent;
    private TextView mTitleTextView;
    private TextView mDetailTextView;
    private TextView mDistanceTextView;
    private RatingBar mBadRateRatingBar;
    private RatingBar mBadRateSkyRatingBar;
    private TextView mBadRateTextView;
    private TextView mBadRateSkyTextView;
    private ImageView mImageView;
    private CardView mCardView;

    public Content getBindContent() {
        return mContent;
    }

    public boolean hasBindContent() {
        return mContent != null;
    }

    public ContentHolder(MainActivity activity, View itemView) {
        super(itemView);
        mActivity = activity;

        mCardView = (CardView) itemView.findViewById(R.id.cardview);
        mCardView.setOnClickListener(new OnContentClick(mActivity, this));

        mTitleTextView = (TextView)
                itemView.findViewById(R.id.textview_content_title);
        mDetailTextView = (TextView)
                itemView.findViewById(R.id.textview_content_detail);
        mDistanceTextView = (TextView)
                itemView.findViewById(R.id.textview_content_distance);
        mBadRateRatingBar = (RatingBar)
                itemView.findViewById(R.id.ratingbar_content_bad_rate);
        mBadRateSkyRatingBar = (RatingBar)
                itemView.findViewById(R.id.ratingbar_content_bad_rate_sky);
        mBadRateTextView = (TextView)
                itemView.findViewById(R.id.textview_content_bad_rate);
        mBadRateSkyTextView = (TextView)
                itemView.findViewById(R.id.textview_content_bad_rate_sky);
        mImageView = (ImageView)
                itemView.findViewById(R.id.imageview_content_image);

    }

    public void bindContent(Content content) {
        mContent = content;
        mTitleTextView.setText(mContent.getTitle());
        mDistanceTextView.setText(String.valueOf(mContent.getDist() + "m"));
        mBadRateRatingBar.setRating(mContent.getUnlike());
        mBadRateSkyRatingBar.setRating(mContent.getUnlikeSky());
        mBadRateTextView.setText(String.format("BAD RATE WITHOUT\nSKY : %.02f",mContent.getUnlike() * 4.0));
        mBadRateSkyTextView.setText(String.format("BAD RATE WITH\nSKY : %.02f",mContent.getUnlikeSky() * 4.0));

        String loadedDetail = mContent.getLoadedDetail();
        if (loadedDetail == null) {

        }else{
            mDetailTextView.setText(loadedDetail);
        }

        Drawable loadedImage = mContent.getLoadedImage();
        if (loadedImage == null) {
            mImageView.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.no_image));
        }else{
            mImageView.setImageDrawable(loadedImage);
        }

        DebugUtil.logD(TAG,String.format("bind content %s", mContent.getContentID()));
    }

    public void bindDetail(String detail) {
        mContent.setLoadedDetail(detail);
        mDetailTextView.setText(detail);
    }

    public void bindPhoto(Drawable drawable) {
        mContent.setLoadedImage(drawable);
        mImageView.setImageDrawable(drawable);
    }

}