package com.root.onvif.model;

import javax.servlet.http.HttpServletResponse;

public class ResponseInfo {
    public static final int OPEN = 1;
    public static final int RUN = 2;
    public static final int CLOSE = 3;

//    public String key;
    public String url;
    public int state;
    public HttpServletResponse response;

//    public String getKey() {
//        return key;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }
}
