package com.example.cxs.baseapp.manager.http;

import android.util.Log;
import com.example.cxs.baseapp.manager.EventManager;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by chengxunshi on 16/9/18.
 */
public class HttpManager {

    public static void sendRequest(final String url, final Type type){

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String body = response.body().string();
                Log.i("HttpManager", "*****" + body + "******");
                Gson gson = new Gson();
                try {
                    ResponseBean responseBean = gson.fromJson(body, type);
                    if (200 != responseBean.code || null == responseBean.data) {
                        ErrorBean errorBean = new ErrorBean(url, responseBean.code, responseBean.msg);
                        EventManager.post(errorBean);
                    } else {
                        EventManager.post(responseBean.data);
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    ErrorBean errorBean = new ErrorBean(url, -2000, "解析失败,请重试!");
                    EventManager.post(errorBean);
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("HttpManager", "******sendRequest:onFailure******");
                e.printStackTrace();
                ErrorBean errorBean = new ErrorBean(url, -1000, "网络请求失败!");
                EventManager.post(errorBean);
            }
        });
    }
}
