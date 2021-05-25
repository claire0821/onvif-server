package com.root.onvif.controller;

import com.root.onvif.model.ResponseInfo;
import com.root.onvif.netty.Server;
import com.root.onvif.rtsp.RtspConverter;
import com.root.onvif.rtsp.RtspState;
import com.root.onvif.service.RtspService;
import com.root.onvif.service.impl.RtspServiceImpl;
import com.root.onvif.thread.AsyncService;
import com.root.onvif.thread.AsyncServiceImpl;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/rtsp")
@CrossOrigin
public class RtspController {
    private static final Logger logger = LoggerFactory.getLogger(RtspConverter.class);
    @Autowired
    RtspService rtspService;
    @Autowired
    AsyncService asyncService;


    @PostConstruct
    public void init() {
//        asyncService.sendFlv();
        loadFFmpeg();
        asyncService.startServer(9998);
        asyncService.send();
        asyncService.clearClient(1000 * 60 * 5, 1000 * 60 * 1);
    }
    public void loadFFmpeg() {
        try {
            logger.info("ffmpeg正在初始化资源，请稍等...");
            FFmpegFrameGrabber.tryLoad();
            FFmpegFrameRecorder.tryLoad();
            logger.info("ffmpeg初始化成功");
        } catch (org.bytedeco.javacv.FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (org.bytedeco.javacv.FrameRecorder.Exception e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/play")
    public void play(HttpServletResponse httpServletResponse) throws IOException, InterruptedException {
        String url = "rtsp://admin:admin123456@10.20.30.104:554/type=0&amp;id=1";//"rtsp://admin:admin123456@10.20.30.104:554/type=0&amp;id=1";//rtsp://system:system@192.168.30.102:554/bs0";//rtsp://10.20.30.176:554/PR0";
        /*

        rtspService.open(url,httpServletResponse);
        RtspConverter rtspConverter = RtspServiceImpl.listRtsp.get(url);
        while (rtspConverter.getHeader() != null) {
//            httpServletResponse.getOutputStream().write(rtspConverter.getHeader());
        }
        Thread.sleep(1000);
        rtspConverter.getOut(httpServletResponse);
//        rtspService.addClient("rtsp://10.20.30.176:554/PR0",httpServletResponse);*/
        String key = asyncService.open(url,httpServletResponse);

        RtspConverter rtspConverter = AsyncServiceImpl.listRtsp.get(url);
//        while (rtspConverter.getHeader() != null) {
//            httpServletResponse.getOutputStream().write(rtspConverter.getHeader());
//        }

//        ResponseInfo responseInfo = AsyncServiceImpl.responseInfos.get(key);
//        if(responseInfo != null) {
//            while (responseInfo.getState() != ResponseInfo.CLOSE) {
//                Thread.sleep(1000);
//            }
//        }

        while (rtspConverter.getRtspState() == RtspState.INITIAL) {
            Thread.sleep(1000);
        }
        if(rtspConverter.getRtspState() == RtspState.CLOSE) {//打开流失败
            return;
        }

        httpServletResponse.getOutputStream().write(rtspConverter.getHeader());//打开流成功，发送flv头


        while (rtspConverter.getRtspState() != RtspState.RUN) {
            Thread.sleep(500);
        }
        if(rtspConverter.getRtspState() == RtspState.RUN) {//正在推流
            asyncService.sendFlv();
        }

        while (rtspConverter.getRtspState() != RtspState.CLOSE) {
            Thread.sleep(6000);
        }
//        send1(rtspConverter,httpServletResponse);
//        asyncService.send(url,httpServletResponse);
        System.out.println("退出");
//        AsyncServiceImpl.responseInfos.remove(key);

    }

    public void send1(RtspConverter rtspConverter,HttpServletResponse response) {
        byte[] outData = rtspConverter.getOutData();
        if(outData != null && outData.length > 0) {
            try {
                response.getOutputStream().write(outData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        send1(rtspConverter,response);
    }
}

