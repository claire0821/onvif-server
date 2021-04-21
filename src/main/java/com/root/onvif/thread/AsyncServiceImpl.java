package com.root.onvif.thread;

import com.root.onvif.model.OnvifDeviceInfo;
import com.root.onvif.service.CapabilitiesService;
import com.root.onvif.util.DiscoveryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
