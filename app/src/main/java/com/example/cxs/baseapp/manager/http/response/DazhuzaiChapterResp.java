package com.example.cxs.baseapp.manager.http.response;

import java.io.Serializable;

/**
 * Created by chengxunshi on 2016/8/11.
 */
public class DazhuzaiChapterResp implements Serializable {
    public Chapter chapter;

    public static class Chapter implements Serializable {
        public String title;
        public String body;
        public String cpContent;

        public Chapter(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public Chapter(String title, String body, String cpContent) {
            this.title = title;
            this.body = body;
            this.cpContent = cpContent;
        }
    }
}
