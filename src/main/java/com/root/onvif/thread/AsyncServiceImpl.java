package com.root.onvif.thread;

import com.root.onvif.model.OnvifDeviceInfo;
import com.root.onvif.model.ResponseInfo;
import com.root.onvif.rtsp.RtspConverter;
import com.root.onvif.service.CapabilitiesService;
import com.root.onvif.util.DiscoveryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class AsyncServiceImpl implements AsyncService{
    @Autowired
    DiscoveryUtils discoveryUtils;

    public static List<Object> IPDiscovery = new ArrayList<>();

    public static List<OnvifDeviceInfo> deviceInfoList = new ArrayList<>();

    @Override
    @Async("asyncServiceExecutor")
    public void updateDevInfo() {
        try {
            while (true) {
                while (IPDiscovery.size() > 0) {
                    String[] ip = (String[])IPDiscovery.get(0);
                    IPDiscovery.remove(0);
                    if(ip[0].length() > 0 && ip[1].length() > 0) {
                        String str = ip[1];
                        int startIndex = str.indexOf(":");
                        String strIP = "";
                        int port = 80;
                        if(startIndex > 0) {//ip加端口
                            strIP = str.substring(0,startIndex);
                            port = Integer.parseInt(str.substring(startIndex + 1,str.length()));
                        } else {
                            strIP = str;
                            port = 80;
                        }
                        OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
                        onvifDeviceInfo.setIp(strIP);
                        onvifDeviceInfo.setPort(port);
                        onvifDeviceInfo.setOnvifAddress(ip[0]);

                        CapabilitiesService.getDeviceInfomation(onvifDeviceInfo);

                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void addSearchIP(String[] ip) {
        IPDiscovery.add(ip);
    }

    //rtsp
    public static Map<String, RtspConverter> listRtsp = new HashMap<>();
    Map<String, List<HttpServletResponse>> listClient = new HashMap<>();
    public static Map<String, ResponseInfo> responseInfos = new HashMap<>();

    @Override
    public String open(String url, HttpServletResponse response){
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

        RtspConverter rtsp = listRtsp.get(url);
        if(rtsp == null) {
            rtsp = new RtspConverter(url);
            listRtsp.put(url,rtsp);
            ExecutorConfig.executor.execute(rtsp);
        }

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setState(ResponseInfo.OPEN);
        responseInfo.setUrl(url);
        responseInfo.setResponse(response);
        String key = UUID.randomUUID().toString();

        responseInfos.put(key,responseInfo);

        return key;
    }

    @Override
    @Async("asyncServiceExecutor")
    public void sendFlv() {
        while (true) {
            try {
                Iterator<Map.Entry<String, ResponseInfo>> entries = responseInfos.entrySet().iterator();
                while(entries.hasNext()){
                    Map.Entry<String, ResponseInfo> entry = entries.next();
                    String key = entry.getKey();
                    ResponseInfo value = entry.getValue();
                    RtspConverter rtspConverter = listRtsp.get(value.getUrl());
                    byte[] outData = rtspConverter.getOutData();
                    if(outData != null && outData.length > 0) {
                        HttpServletResponse response = value.getResponse();
                        response.getOutputStream().write(outData);
                        value.setState(ResponseInfo.RUN);
                    }
                }
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void send(String url, HttpServletResponse response) {
        RtspConverter rtspConverter = listRtsp.get(url);
        byte[] outData = rtspConverter.getOutData();
        if(outData != null && outData.length > 0) {
            try {
                response.getOutputStream().write(outData);
                Thread.sleep(1000);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        send(url,response);
    }
}
