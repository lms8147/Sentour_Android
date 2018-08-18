package com.tou4u.sentour.contentviewer.fragment;

import android.app.Activity;

import android.content.Context;

import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tou4u.sentour.util.DebugUtil;
import com.tou4u.sentour.R;
import com.tou4u.sentour.contentviewer.ContentAdapter;
import com.tou4u.sentour.contentviewer.ContentLoadManager;
import com.tou4u.sentour.contentviewer.ContentsLoadManager;
import com.tou4u.sentour.contentviewer.main.MainActivity;


public class ContentListFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private static final String ARG_CONTENT_TYPE = "contentType";

    public static final int RADIUS = 10000;

    private int mContentType;

    private ContentAdapter mAdapter;
    private ContentLoadManager mContentLoadManager;
    private ContentsLoadManager mContentsLoadManager;

    private RecyclerView mContentRecyclerView;

    private MainActivity mActivity;

    private volatile boolean canUpdate = false;


    /**
     * 프레그먼트가 엑티비티의 프레그먼트 매니저에 등록될때 실행되는 메소드
     * 프레그먼트를 호스팅한 엑티비티를 CallBack 인터페이스로 업캐스팅 후 멥버변수로 등록
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;
            if (a instanceof MainActivity) {
                mActivity = (MainActivity) a;
            }
        }

    }

    /**
     * 프레그먼트가 소멸될때 등록 된 콜백 메소드를 해제
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public static ContentListFragment newInstance(int contentType) {
        Bundle args = new Bundle();
        args.putInt(ARG_CONTENT_TYPE, contentType);
        ContentListFragment fragment = new ContentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        canUpdate = false;
        mContentLoadManager.clearQueue();
        DebugUtil.logD(TAG,"content load manager clear");
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugUtil.logD(TAG,"onResume");
        canUpdate = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContentsLoadManager.cancle();
        mContentLoadManager.quit();
        DebugUtil.logD(TAG,"Background thread destroyed");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentType = getArguments().getInt(ARG_CONTENT_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);

        mContentRecyclerView = (RecyclerView) view
                .findViewById(R.id.content_recycler_view);

        mContentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mContentLoadManager = new ContentLoadManager(mActivity);
        mAdapter = new ContentAdapter(mActivity, mContentLoadManager);
        mContentRecyclerView.setAdapter(mAdapter);

        mContentsLoadManager = new ContentsLoadManager(mAdapter);

        return view;
    }

    public void updateUI(String lat, String lon, String sky, String hashTag) {

        if(!canUpdate){
            DebugUtil.logD(TAG,"update fail -> view not ready");
            return;
        }

        if (lat != null && lon != null && sky != null && hashTag != null && lat != null)
            mContentsLoadManager.loadContents(lat, lon, sky, mContentType, hashTag);
        else
            DebugUtil.logD(TAG,"update fail -> some null");
    }

}
