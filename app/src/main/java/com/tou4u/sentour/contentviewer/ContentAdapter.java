package com.tou4u.sentour.contentviewer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tou4u.sentour.R;
import com.tou4u.sentour.contentviewer.main.MainActivity;
import com.tou4u.sentour.data.Content;

import java.util.ArrayList;

public class ContentAdapter extends RecyclerView.Adapter<ContentHolder> {

    private MainActivity mActivity;

    private ArrayList<Content> mContents;
    private BindCallBack mBindCallBack;

    public ContentAdapter(MainActivity activity, BindCallBack bindCallBack) {
        super();
        mActivity = activity;
        mBindCallBack = bindCallBack;
        mContents = new ArrayList<>();
    }

    interface BindCallBack {
        void onBind(ContentHolder holder, String url, String contentID);
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mActivity);
        View view = layoutInflater
                .inflate(R.layout.list_item_content, parent, false);
        return new ContentHolder(mActivity, view);
    }

    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        Content content = mContents.get(position);
        holder.bindContent(content);
        mBindCallBack.onBind(holder, content.getFirstImageUrl(), content.getContentID());
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    public void changeContents(ArrayList<Content> contents) {
        mContents = contents;
        notifyDataSetChanged();
    }

}