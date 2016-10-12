package com.example.cxs.baseapp.manager.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class DazhuzaiResponse {

    public List<Chapter> chapterList;

    public static class Chapter implements Parcelable {
        public String url;
        public String title;

        @Override
        public int describeContents() {
            return 0;
        }

        public Chapter(Parcel in) {
            url = in.readString();
            title = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(url);
            dest.writeString(title);
        }

        public static final Parcelable.Creator<Chapter> CREATOR =
            new Parcelable.Creator<Chapter>() {
                @Override
                public Chapter createFromParcel(Parcel source) {
                    return new Chapter(source);
                }

                @Override
                public Chapter[] newArray(int size) {
                    return new Chapter[0];
                }
            };
    }
}
