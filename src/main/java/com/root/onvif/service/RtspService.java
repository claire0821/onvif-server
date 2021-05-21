package com.root.onvif.service;

import javax.servlet.http.HttpServletResponse;

public interface RtspService {
    void open(String url,HttpServletResponse response);
    void addClient(String url,HttpServletResponse httpServletResponse);
}
