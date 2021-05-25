package com.root.onvif.thread;

import com.root.onvif.model.OnvifDeviceInfo;
import com.root.onvif.model.ResponseInfo;
import com.root.onvif.model.StreamInfo;
import com.root.onvif.netty.Server;
import com.root.onvif.rtsp.RtspConverter;
import com.root.onvif.rtsp.RtspState;
import com.root.onvif.service.CapabilitiesService;
import com.root.onvif.util.DiscoveryUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
public class AsyncServiceImpl implements AsyncService{
    @Autowired
    DiscoveryUtils discoveryUtils;
    @Autowired
    Server server;

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
//    public static Map<String, ResponseInfo> responseInfos = new HashMap<>();
    public static List<ResponseInfo> listRes = new ArrayList<>();

    public static List<StreamInfo> listStream = new ArrayList<>();
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
//        responseInfo.setResponse(response);
        String key = UUID.randomUUID().toString();

        return key;
    }

    public static void addRes(ResponseInfo responseInfo) {
        RtspConverter rtsp = listRtsp.get(responseInfo.getUrl());
        if(rtsp == null) {
            rtsp = new RtspConverter(responseInfo.getUrl());
            listRtsp.put(responseInfo.getUrl(),rtsp);
            ExecutorConfig.executor.execute(rtsp);
        }
        listRes.add(responseInfo);
    }
    @Override
    @Async("asyncServiceExecutor")
    public void sendFlv() {
        while (true) {

//            // ws输出帧流
//            for (Map.Entry<String, ChannelHandlerContext> entry : ServerHandler.wsClients.entrySet()) {
//                try {
//                    if (entry.getValue().channel().isWritable()) {
//                        entry.getValue()
//                                .writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer()));
//                    } else {
//                        ServerHandler.wsClients.remove(entry.getKey());
//                    }
//                } catch (java.lang.Exception e) {
//                    ServerHandler.wsClients.remove(entry.getKey());
//
//                    e.printStackTrace();
//                }
//            }

//            try {
//                Iterator<Map.Entry<String, ResponseInfo>> entries = responseInfos.entrySet().iterator();
//                while(entries.hasNext()){
//                    Map.Entry<String, ResponseInfo> entry = entries.next();
//                    String key = entry.getKey();
//                    ResponseInfo value = entry.getValue();
//                    RtspConverter rtspConverter = listRtsp.get(value.getUrl());
//                    byte[] outData = rtspConverter.getOutData();
//                    if(outData != null && outData.length > 0) {
//                        HttpServletResponse response = value.getResponse();
//                        response.getOutputStream().write(outData);
//                        value.setState(ResponseInfo.RUN);
//                    }
//                }
//                Thread.sleep(50);
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
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

    @Override
    @Async("asyncServiceExecutor")
    public void send() {
        while (true) {
            if(listStream.size() <= 0) continue;
            StreamInfo streamInfo = listStream.get(0);
            if(streamInfo == null) continue;
            listStream.remove(0);

            if(streamInfo.getState() == RtspState.CLOSE) {//流关闭
                for (ResponseInfo responseInfo : listRes) {
                    if(responseInfo.getUrl().equals(streamInfo.getUrl())) {
                        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
                                Unpooled.copiedBuffer("视频流异常: " + HttpResponseStatus.BAD_REQUEST + "\r\n", CharsetUtil.UTF_8));
                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                        responseInfo.getResponse().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                        listRes.remove(responseInfo);
                    }
                }
            } else if(streamInfo.getState() == RtspState.RUN) {//正在解码发送流
//                System.out.println("send");
                for(ResponseInfo responseInfo : listRes) {
                    if(responseInfo.getUrl().equals(streamInfo.getUrl())) {
                        if(!responseInfo.isSendHeader()) {//先发送头
                            if(responseInfo.getIsHttp() == ResponseInfo.HTTP) {
                                responseInfo.getResponse().writeAndFlush(Unpooled.copiedBuffer(streamInfo.getHeaders()));
                            } else {
                                responseInfo.getResponse().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(streamInfo.getHeaders())));
                            }
                            responseInfo.setSendHeader(true);
                        }
                        if(streamInfo.getOutData() != null) {
                            if(responseInfo.getIsHttp() == ResponseInfo.HTTP) {
                                responseInfo.getResponse().writeAndFlush(Unpooled.copiedBuffer(streamInfo.getOutData()));
                            } else {
                                responseInfo.getResponse().writeAndFlush(new BinaryWebSocketFrame(Unpooled.copiedBuffer(streamInfo.getOutData())));
                            }

                        }
                    }
                }
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void remove(ChannelId id){
        String url = "";
        for(ResponseInfo responseInfo : listRes) {
            if(responseInfo.getResponse().channel().id().equals(id)) {
                url = responseInfo.getUrl();
                listRes.remove(responseInfo);
                break;
            }
        }

        int count = 0;
        if(url.length() > 0) {
            for(ResponseInfo responseInfo : listRes) {
                if(responseInfo.getUrl().equals(url)) {
                    count++;
                }
            }
        }
        if(count == 0) {
            RtspConverter rtspConverter = listRtsp.get(url);
            listRtsp.remove(url);
            rtspConverter.exit = true;
        }
    }
    //开启netty服务器
    @Override
    @Async("asyncServiceExecutor")
    public void startServer(int port) {
        server.start(port);
    }

    //定时清除连接
    @Override
    @Async("asyncServiceExecutor")
    public void clearClient(long sleep, long timeOut) {
        while (true) {
            try{
                for(ResponseInfo responseInfo : listRes) {
                    if(responseInfo.getResponse().channel().isWritable()) {
                        if(responseInfo.getIsHttp() == ResponseInfo.WS){//websocket超时发送ping
                            responseInfo.getResponse().writeAndFlush(new PingWebSocketFrame());
                        }
                    }
                }
                for(ResponseInfo responseInfo : listRes) {
                    long beginMillisecond = responseInfo.getLastTime().getTime();
                    Date now = new Date();
                    long nowMillisecond = now.getTime();
                    if(nowMillisecond >= beginMillisecond) {
                        long time = nowMillisecond - beginMillisecond;
                        if(time > timeOut) {//超时处理
                            responseInfo.getResponse().close();
//                            listRes.remove(responseInfo);
                        }
                    }
                }

                Thread.sleep(sleep);
            }catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

        }
    }

}
