package com.tou4u.sentour.contentviewer.main;

interface MainDataObserver {
    void update(String lat, String lng, String hashTag, String sky, int currentPage);
}
