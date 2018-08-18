package com.tou4u.sentour.data;

import android.graphics.drawable.Drawable;

public class Content {


    private String contentID;
    private int dist;
    private String title;
    private String contentTypeID;
    private String firstImageUrl;
    private String overview;
    private float unlike;
    private float unlike_sky = 0;

    private String loadedDetail;
    private Drawable loadedImage;

    public Drawable getLoadedImage() {
        return loadedImage;
    }

    public String getLoadedDetail() {
        return loadedDetail;
    }

    public void setLoadedDetail(String loadedDetail) {
        this.loadedDetail = loadedDetail;
    }

    public void setLoadedImage(Drawable loadedImage) {
        this.loadedImage = loadedImage;
    }

    private GeoPosition geoPosition;

    public float getUnlike() {
        return unlike;
    }

    public float getUnlikeSky() {
        return unlike_sky;
    }


    public String getContentID() {
        return contentID;
    }

    public int getDist() {
        return dist;
    }

    public String getTitle() {
        return title;
    }

    public String getContentTypeID() {
        return contentTypeID;
    }

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public static final class Builder {
        private Content info;

        public Builder() {
            info = new Content();
            info.loadedDetail = null;
            info.loadedImage = null;
        }

        @Override
        public String toString() {
            return info.contentID;
        }

        public Content build() {
            return info;
        }

        public void setUnlike(float unlike) {
            info.unlike = unlike;
        }

        public void setUnlikeSky(float unlike) {
            info.unlike_sky = unlike;
        }

        public void setContentID(String contentID) {
            info.contentID = contentID;
        }

        public void setDist(int dist) {
            info.dist = dist;
        }

        public void setTitle(String title) {
            info.title = title;
        }

        public void setContentTypeID(String contentTypeID) {
            info.contentTypeID = contentTypeID;
        }

        public void setFirstImageUrl(String firstImageUrl) {
            info.firstImageUrl = firstImageUrl;
        }

        public void setGeoPosition(GeoPosition geoPosition) {
            info.geoPosition = geoPosition;
        }

        public void setOverview(String overview) {
            info.overview = overview;
        }
    }
}
