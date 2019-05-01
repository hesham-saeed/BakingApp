package com.bignerdranch.android.bakingapp2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Steps implements Serializable{


    int id;
    String shortDescription;
    String longDescription;
    String videoURL;
    String thumbnailURL;

    public Steps(int id, String shortDescription, String longDescription, String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
