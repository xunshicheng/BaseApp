package com.example.cxs.baseapp.http;

import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chengxunshi on 16/10/14.
 */
public interface DazhuzaiApi {

    public final static String HOST_URL = "http://api.zhuishushenqi.com/";

    @GET("mix-atoc/51d11e782de6405c45000068")
    Observable<DazhuzaiResponse> getChapterList();

    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<DazhuzaiChapterResp> getChapter(@Path("url") String url);

}
