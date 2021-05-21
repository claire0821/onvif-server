package com.root.onvif.service.impl;

import com.root.onvif.rtsp.RtspConverter;
import com.root.onvif.service.RtspService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.root.onvif.thread.ExecutorConfig;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class RtspServiceImpl implements RtspService {
    public static Map<String, RtspConverter> listRtsp = new HashMap<>();
    Map<String, List<HttpServletResponse>> listClient = new HashMap<>();

    @Override
    public void open(String url, HttpServletResponse response) {
        //设置响应头
        response.setContentType("video/x-flv");
        response.setHeader("Connection", "keep-alive");
        response.setStatus(HttpServletResponse.SC_OK);
        //写出缓冲信息，并清空
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RtspConverter rtsp = new RtspConverter(url);
        listRtsp.put(url,rtsp);
        ExecutorConfig.executor.execute(rtsp);
    }

    @Override
    public void addClient(String url,HttpServletResponse response) {
        Cookie cookie = new Cookie("id","1");
        response.addCookie(cookie);
        //设置响应头
        response.setContentType("video/x-flv");
        response.setHeader("Connection", "keep-alive");
        response.setStatus(HttpServletResponse.SC_OK);
        //写出缓冲信息，并清空
        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<HttpServletResponse> httpServletResponses = listClient.get(url);
        if(httpServletResponses == null) {
            httpServletResponses = new ArrayList<>();
        }
        httpServletResponses.add(response);
        listClient.put(url,httpServletResponses);
    }
}
