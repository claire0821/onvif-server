package com.root.onvif.thread;

import javax.servlet.http.HttpServletResponse;

public interface AsyncService {
    void updateDevInfo();
    void addSearchIP(String[] ip);
    String open(String url, HttpServletResponse response);
    void sendFlv();
    void send(String url, HttpServletResponse response);
}
