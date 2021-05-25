package com.root.onvif.thread;

import com.root.onvif.model.ResponseInfo;

import javax.servlet.http.HttpServletResponse;

public interface AsyncService {
    void updateDevInfo();
    void addSearchIP(String[] ip);
    String open(String url, HttpServletResponse response);
    void sendFlv();
    void send(String url, HttpServletResponse response);

    void startServer(int port);

    //添加
//    void addRes(ResponseInfo responseInfo);

    void send();

    void clearClient(long sleep, long timeOut);//定时清除连接
}
