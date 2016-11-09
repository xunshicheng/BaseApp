package com.example.cxs.baseapp.http;

import com.example.cxs.baseapp.manager.http.ResponseBean;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiChapterResp;
import com.example.cxs.baseapp.manager.http.response.DazhuzaiResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by chengxunshi on 16/10/14.
 */
public class RetrofitHelper {

    private OkHttpClient okHttpClient;
    private DazhuzaiApi dazhuzaiApi;
    private static RetrofitHelper instance;

    public RetrofitHelper() {
        initOkHttp();
        dazhuzaiApi = getDazhuzaiApiService();
    }

    public static RetrofitHelper getInstance() {
        if(null == instance) {
            synchronized (RetrofitHelper.class) {
                if(null == instance) {
                    instance = new RetrofitHelper();
                }
            }
        }
        return instance;
    }

    private void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    private DazhuzaiApi getDazhuzaiApiService() {
        Retrofit dazhuzaiRetrofit = new Retrofit.Builder()
                .baseUrl(DazhuzaiApi.HOST_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return dazhuzaiRetrofit.create(DazhuzaiApi.class);
    }

    public Observable<DazhuzaiResponse> getChapterList() {
        return dazhuzaiApi.getChapterList();
    }

    public Observable<DazhuzaiChapterResp> getChapter(String chapterUrl) {
        return dazhuzaiApi.getChapter(chapterUrl);
    }

}
