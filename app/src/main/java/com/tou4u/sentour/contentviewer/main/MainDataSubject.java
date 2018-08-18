package com.tou4u.sentour.contentviewer.main;

import com.tou4u.sentour.data.GeoPosition;
import com.tou4u.sentour.util.DebugUtil;

import java.util.Vector;

public class MainDataSubject {

    private static final String TAG = "MainDataSubject";
    private Vector<MainDataObserver> observers;

    private GeoPosition mGeoPosition;
    private String mHashTag;
    private String mSky;
    private int mCurrentPage;

    private boolean changed;

    public MainDataSubject() {
        observers = new Vector<>();
        mGeoPosition = null;
        mHashTag = null;
        mSky = null;
        mCurrentPage = -1;
    }

    public GeoPosition getPosition(){
        return mGeoPosition;
    }

    public String getFormattedHashTag() {
        return "#" + mHashTag;
    }

    public String getHashTag() {
        return mHashTag;
    }

    public String getSky() {
        return mSky;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.mGeoPosition = geoPosition;
        DebugUtil.logD(TAG, String.format("DataSetChanged %s", mGeoPosition));
        setChanged();
    }

    public void setHashTag(String hashTag) {
        if(hashTag != null){
            if(!hashTag.equals(mHashTag)) {
                this.mHashTag = hashTag.substring(1);
                DebugUtil.logD(TAG, String.format("DataSetChanged %s", mHashTag));
                setChanged();
            }
        }
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage != mCurrentPage){
            this.mCurrentPage = currentPage;
            DebugUtil.logD(TAG, String.format("DataSetChanged %d", mCurrentPage));
            setChanged();
        }
    }

    public boolean setSky(String sky) {
        if(sky != null){
            if(!sky.equals(mSky)){
                this.mSky = sky;
                DebugUtil.logD(TAG, String.format("DataSetChanged %s", mSky));
                setChanged();
                return true;
            }
        }
        return false;
    }

    public void addObserver(MainDataObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void removeObserver(MainDataObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void notifyObservers() {
        if (changed) {
            synchronized (observers) {
                for (MainDataObserver observer : observers)
                    observer.update(mGeoPosition.getLat(), mGeoPosition.getLon(), mHashTag, getSky(), mCurrentPage);
            }
        }
    }

    public void setChanged() {
        if (isVaild()) {
            changed = true;
            notifyObservers();
        }
    }

    public boolean isVaild(){
        return mGeoPosition != null && mHashTag != null && mSky != null && mCurrentPage != -1;
    }
}

