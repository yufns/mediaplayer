package com.example.gsyvideoplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shuyu on 2016/12/7.
 */

public class SwitchVideoModel implements Parcelable {
    private String url;
    private String name;

    public SwitchVideoModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
    }

    protected SwitchVideoModel(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<SwitchVideoModel> CREATOR =
        new Parcelable.Creator<SwitchVideoModel>() {
            @Override public SwitchVideoModel createFromParcel(Parcel source) {
                return new SwitchVideoModel(source);
            }

            @Override public SwitchVideoModel[] newArray(int size) {
                return new SwitchVideoModel[size];
            }
        };
}