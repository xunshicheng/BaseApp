
package com.example.cxs.baseapp.manager.http;

public class ErrorBean {

    public String apiName;

    public int code;

    public String msg;

    public ErrorBean(String apiName, int code, String msg) {

        this.apiName = apiName;

        this.code = code;

        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{apiName:" + apiName + ", code:" + code + ", msg:" + msg + "}";
    }
}
