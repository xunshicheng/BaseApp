package com.example.cxs.baseapp.http;

import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by chengxunshi on 16/10/14.
 */
public interface DazhuzaiApi {

    public final static String HOST_URL = "http://45.78.39.97:8080/";

    @GET("dazhuzai")
    Observable<ResponseBean<DazhuzaiResponse>> getChapterList();

    @GET("dazhuzai/{chapter}")
    Observable<ResponseBean<DazhuzaiChapterResp>> getChapter(@Path("chapter") String chapter);
}
