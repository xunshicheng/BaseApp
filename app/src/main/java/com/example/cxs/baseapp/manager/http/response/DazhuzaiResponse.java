package com.example.cxs.baseapp.manager.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class DazhuzaiResponse {

    public MixToc mixToc;

    public static class MixToc {

        public String _id;
        public List<ChapterIntro> chapters;

        public static class ChapterIntro {
            public String title;
            public String link;
        }

    }
}
