package com.root.onvif.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.root.onvif.mbg.model.TbDev;
import com.root.onvif.model.OnvifDeviceInfo;
import com.root.onvif.mysql.DevService;
import com.root.onvif.service.CapabilitiesService;
import com.root.onvif.thread.AsyncService;
import com.root.onvif.util.DiscoveryUtils;
import com.root.onvif.util.ResultCode;
import com.root.onvif.util.ResultUtil;
import org.apache.catalina.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.sound.sampled.Port;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.zip.DeflaterOutputStream;

@RestController
@RequestMapping("/onvif")
@CrossOrigin
public class DiscoveryController {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryController.class);

    public static LinkedHashMap<String,OnvifDeviceInfo> deviceInfoList;//存放onvif设备信息
    @Autowired
    DiscoveryUtils discoveryUtils;
    @Autowired
    AsyncService asyncService;
    @Autowired
    DevService devService;//记录设备
    @PostConstruct
    private void init() {
//        asyncService.updateDevInfo();
//        List<InetAddress> localIP = discoveryUtils.getLocalIP();
//        for(int i = 0; i < localIP.size(); i++) {
//            discoveryUtils.discovery(localIP.get(i),10);
//        }
        loadDev();
        getLocalIP(true);
    }

    //#region 加载数据库设备
    private void loadDev() {
        deviceInfoList = new LinkedHashMap<String,OnvifDeviceInfo>();
        List<TbDev> tbDevs = devService.listAll();
        if(tbDevs == null || tbDevs.size() <= 0) {
            return;
        }
        for (TbDev tbDev : tbDevs) {
            OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
            onvifDeviceInfo.setIp(tbDev.getIp());
            onvifDeviceInfo.setPort(tbDev.getPort());
            onvifDeviceInfo.setUsername(tbDev.getUsername());
            onvifDeviceInfo.setPassword(tbDev.getPassword());
            onvifDeviceInfo.setOnvifAddress(tbDev.getOnvifAddress());
            onvifDeviceInfo.setMediaUrl(tbDev.getMediaUrl());
            onvifDeviceInfo.setImagingUrl(tbDev.getImagingUrl());
            onvifDeviceInfo.setEventsUrl(tbDev.getEventsUrl());
            onvifDeviceInfo.setDeviceUrl(tbDev.getDeviceUrl());
            onvifDeviceInfo.setPtzUrl(tbDev.getPtzUrl());
            onvifDeviceInfo.setAnalyticsUrl(tbDev.getAnalyticsUrl());

            deviceInfoList.put(tbDev.getIp(),onvifDeviceInfo);
        }
    }
    //#endregion

    //#region 设备管理
    @RequestMapping(value = "addDev",method = RequestMethod.GET)
    public JSONObject addDev(@RequestParam(value = "address",required = true) String address,
                             @RequestParam(value = "username",required = false,defaultValue = "") String username,
                             @RequestParam(value = "password",required = false,defaultValue = "") String password) {

        if(address.length() <= 0) {
            return ResultUtil.error();
        }
        String[] split = address.split("/");
        if(split.length < 2) {
            return ResultUtil.error();
        }

        OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
        String str = split[2];
        int startIndex = str.indexOf(":");
        if(startIndex != -1) {
            String ip = str.substring(0,startIndex);
            String port = str.substring(startIndex + 1,str.length());
            int iPort = Integer.parseInt(port);

            onvifDeviceInfo.setIp(ip);
            onvifDeviceInfo.setPort(iPort);
        } else {
            onvifDeviceInfo.setIp(str);
            onvifDeviceInfo.setPort(80);
        }

        onvifDeviceInfo.setOnvifAddress(address);
        onvifDeviceInfo.setUsername(username);
        onvifDeviceInfo.setPassword(password);

        try {
            onvifDeviceInfo = CapabilitiesService.getCapabilities(onvifDeviceInfo);
            updateMysqlDev(onvifDeviceInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success(onvifDeviceInfo);
    }

    @RequestMapping(value = "/getDevList", method = RequestMethod.GET)
    public JSONObject getDevList() {
        List<TbDev> tbDevs = devService.listAll();
        if(tbDevs == null) {
            return ResultUtil.error("加载设备列表出错");
        }
        if(tbDevs.size() == 0) {
            return ResultUtil.error("设备列表为空");
        }

        return ResultUtil.success(tbDevs);
    }
    //#endregion
    //#region 本地ip段

    /**
     * 获取本地ip段
     * @param update 是否重新获取
     * @return
     */
    @RequestMapping(value = "/getLocalIP", method = RequestMethod.GET)
    public JSONObject getLocalIP(@RequestParam(value = "update",required = false,defaultValue = "false") Boolean update) {
        if(update) {//重新获取本地ip段
            discoveryUtils.addressList.clear();
        }
        if(discoveryUtils.addressList.size() > 0) {
            return ResultUtil.success(discoveryUtils.addressList);
        }
        discoveryUtils.getLocalIP();
        if(discoveryUtils.addressList.size() > 0) {
            return ResultUtil.success(discoveryUtils.addressList);
        }
        return ResultUtil.error(ResultCode.GET_LOCAL_IP_SEGMENT_FAILED);
    }
    //#endregion

    //#region 搜索设备

    /**
     * 根据ip段搜索设备
     * @param ipSegment ip
     * @return
     */
    @RequestMapping(value = "getDevByIpSegment",method = RequestMethod.GET)
    public JSONObject getDevByIpSegment(@RequestParam(value = "ipSegment") String ipSegment) {
        InetAddress address = null;

        for (InetAddress inetAddress:
        discoveryUtils.addressList) {
            if(inetAddress.getHostAddress().equals(ipSegment)) {
                address = inetAddress;
            }
        }

        if(address == null) {
            return ResultUtil.error(ResultCode.NO_SUCE_IP_SEGMENT);
        }
        List<Object> discovery = discoveryUtils.discovery(address, 10);
        if(discovery.size() <= 0) {
            return ResultUtil.error(ResultCode.NO_DEVICE_FOUND);
        }
        for(Object object : discovery) {
            String url = ((String[])object)[0];
            String ip = ((String[])object)[1];

            int startIndex = ip.indexOf(":");
            String strIP = "";
            int port = 80;
            if(startIndex > 0) {//ip加端口
                strIP = ip.substring(0,startIndex);
                port = Integer.parseInt(ip.substring(startIndex + 1,ip.length()));
            } else {
                strIP = ip;
                port = 80;
            }
            OnvifDeviceInfo onvifDeviceInfo = new OnvifDeviceInfo();
            onvifDeviceInfo.setIp(strIP);
            onvifDeviceInfo.setPort(port);
            onvifDeviceInfo.setOnvifAddress(url);
            if(deviceInfoList.get(ip) == null) {
                deviceInfoList.put(strIP,onvifDeviceInfo);
            }
        }
        return ResultUtil.success(discovery);
    }
    //#endregion

    //#region dev
    /**
     * 获取能力集url
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/getCapabilities",method = RequestMethod.GET)
    public JSONObject getCapabilities(@RequestParam(value = "ip",required = true) String ip,
                                      @RequestParam(value = "port",required = false,defaultValue = "80") int port,
                                      @RequestParam(value = "username",required = false) String username,
                                      @RequestParam(value = "password", required = false) String password) {
        OnvifDeviceInfo onvifDeviceInfo = getDevInfoFromList(ip, port, username, password);

        try {
            OnvifDeviceInfo capabilities = CapabilitiesService.getCapabilities(onvifDeviceInfo);
            updateMysqlDev(capabilities);
            return ResultUtil.success(capabilities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }

    /**
     * 根据ip获取设备信息
     * @param ip
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/getDevInfo",method = RequestMethod.GET)
    public JSONObject getDevInfo(@RequestParam(value = "ip",required = true) String ip,
                                 @RequestParam(value = "port",required = false,defaultValue = "80") int port,
                                 @RequestParam(value = "username",required = false) String username,
                                 @RequestParam(value = "password", required = false) String password) {

        OnvifDeviceInfo onvifDeviceInfo = getDevInfoFromList(ip, port, username, password);

        try {
            Map<String, String> deviceInfomation = CapabilitiesService.getDeviceInfomation(onvifDeviceInfo);
            return ResultUtil.success(deviceInfomation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtil.error();

    }
    //#endregion

    //#region media
    @RequestMapping(value = "/getMediaProfiles",method = RequestMethod.GET)
    public JSONObject getMediaProfiles(@RequestParam(value = "ip",required = true) String ip,
                                        @RequestParam(value = "port",required = false,defaultValue = "80") int port,
                                        @RequestParam(value = "username",required = false) String username,
                                        @RequestParam(value = "password", required = false) String password) {

        OnvifDeviceInfo onvifDeviceInfo = getDevInfoFromList(ip, port, username, password);
        try {
            Map<String,String> mediaProfiles = CapabilitiesService.getMediaProfiles(onvifDeviceInfo);
            if(mediaProfiles == null) return ResultUtil.error();

            JSONArray jsonArray = new JSONArray();
            Iterator entries = mediaProfiles.entrySet().iterator();
            while(entries.hasNext()){
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String value = (String)entry.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token",key);
                jsonObject.put("name",value);
                jsonArray.add(jsonObject);
            }
            return ResultUtil.success(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error();
    }


    @RequestMapping(value = "/getSnapshotUri", method = RequestMethod.GET)
    public JSONObject getSnapshotUri(@RequestParam(value = "ip",required = true) String ip) {
        OnvifDeviceInfo onvifDeviceInfo = getDevInfoFromList(ip, 80, null, null);

        try {
            Map<String,String> mediaProfiles = CapabilitiesService.getMediaProfiles(onvifDeviceInfo);
            if(mediaProfiles == null) return ResultUtil.error();

            JSONArray jsonArray = new JSONArray();
            Iterator entries = mediaProfiles.entrySet().iterator();
            while(entries.hasNext()){
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String value = (String)entry.getValue();

                String snapshotUri = CapabilitiesService.getSnapshotUri(onvifDeviceInfo, key);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token",key);
                jsonObject.put("name",value);
                jsonObject.put("uri",snapshotUri);
                jsonArray.add(jsonObject);
            }
            return ResultUtil.success(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultUtil.error();
    }

    @RequestMapping(value = "/getStreamUri", method = RequestMethod.GET)
    public JSONObject getStreamUri(@RequestParam(value = "ip",required = true) String ip,
                                   @RequestParam(value = "profileToken",required = true) String profileToken) {
        OnvifDeviceInfo onvifDeviceInfo = deviceInfoList.get(ip);
        if(onvifDeviceInfo == null) {
            return ResultUtil.error();
        }

        try {
            String streamUri = CapabilitiesService.getStreamUri(onvifDeviceInfo, profileToken);
            return ResultUtil.success((Object) streamUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }
    //#endregion

    //#region ptz

    /**
     * 云台控制
     * @param ip 设备ip
     * @param command 0-停止 1-上 2-下 3-左 4-右 5-左上 6-左下 7-右上 8-右下 9-镜头拉近 10-镜头推远 11-光圈放大 12-光圈缩小 13-焦距增加 14-焦距减小
     * @param speed 速度
     * @return
     */
    @RequestMapping(value = "/ptzControl", method = RequestMethod.GET)
    public JSONObject ptzControl(@RequestParam(value = "ip",required = true) String ip,
                                 @RequestParam(value = "command",required = true) Integer command,
                                 @RequestParam(value = "speed",required = false,defaultValue = "0.5") Float speed,
                                 @RequestParam(value = "profileToken",required = true) String profileToken) {
        OnvifDeviceInfo onvifDeviceInfo = deviceInfoList.get(ip);
        if(onvifDeviceInfo == null) {
            return ResultUtil.error();
        }

        try {
            CapabilitiesService.continuousMove(onvifDeviceInfo,command,profileToken, speed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.success();
    }
    //#endregion
    /**
     * 从设备列表查找设备，没有则创建
     * @param ip
     * @param port
     * @param username
     * @param password
     * @return
     */
    private OnvifDeviceInfo getDevInfoFromList(String ip, int port, String username, String password) {
        OnvifDeviceInfo onvifDeviceInfo = deviceInfoList.get(ip);

        if(onvifDeviceInfo == null) {//设备列表没有设备
            onvifDeviceInfo = new OnvifDeviceInfo();
        }
        //用户名密码
        if(username != null) {
            onvifDeviceInfo.setUsername(username);
        }
        if(password != null) {
            onvifDeviceInfo.setPassword(password);
        }

        //端口
        if(onvifDeviceInfo.getPort() == 0) {
            onvifDeviceInfo.setPort(port);
        }
        onvifDeviceInfo.setIp(ip);
        if(onvifDeviceInfo.getOnvifAddress().length() == 0) {
            onvifDeviceInfo.setOnvifAddress("http://" + ip + ":" + onvifDeviceInfo.getPort()+ "/onvif/device_service");
        }

        return onvifDeviceInfo;
    }

    private void updateMysqlDev(OnvifDeviceInfo onvifDeviceInfo) {
        TbDev tbDev = new TbDev();
        tbDev.setIp(onvifDeviceInfo.getIp());
        tbDev.setPort(onvifDeviceInfo.getPort());
        tbDev.setUsername(onvifDeviceInfo.getUsername());
        tbDev.setPassword(onvifDeviceInfo.getPassword());
        tbDev.setOnvifAddress(onvifDeviceInfo.getOnvifAddress());
        tbDev.setMediaUrl(onvifDeviceInfo.getMediaUrl());
        tbDev.setImagingUrl(onvifDeviceInfo.getImagingUrl());
        tbDev.setEventsUrl(onvifDeviceInfo.getEventsUrl());
        tbDev.setDeviceUrl(onvifDeviceInfo.getDeviceUrl());
        tbDev.setPtzUrl(onvifDeviceInfo.getPtzUrl());
        tbDev.setAnalyticsUrl(onvifDeviceInfo.getAnalyticsUrl());

        int delete = devService.delete(tbDev.getIp());
        int i = devService.create(tbDev);

    }

    private static final String MULTICAST_IP = "239.255.255.250";
    private static final Integer MULTICAST_PORT = 3702;
    private static final int SOCKET_TIMEOUT_MILL_SECONDS = 1000;
    private static final int DISCOVERY_TIMEOUT_MILL_SECONDS = 3000;
    private static final String SOAP_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<e:Envelope xmlns:e=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:w=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" \n" +
            "\txmlns:d=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\" xmlns:dn=\"http://www.onvif.org/ver10/network/wsdl\">\n" +
            "\t<e:Header>\n" +
            "\t\t<w:MessageID>" +
            "uuid:" + UUID.randomUUID().toString() +
            "</w:MessageID>\n" +
            "\t\t\t<w:To e:mustUnderstand=\"true\">urn:schemas-xmlsoap-org:ws:2005:04:discovery</w:To>\n" +
            "\t\t\t<w:Action a:mustUnderstand=\"true\">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</w:Action>\n" +
            "\t</e:Header>\n" +
            "\t<e:Body>\n" +
            "\t\t<d:Probe>\n" +
            "\t\t\t<d:Types>dn:NetworkVideoTransmitter</d:Types>\n" +
            "\t\t</d:Probe>\n" +
            "\t</e:Body>\n" +
            "</e:Envelope>";
    private MulticastSocket createMulticastGroupAndJoin(String groupUrl, int port, String localIp) throws IOException {
        MulticastSocket socket = null;
        InetAddress group = null;
        try {
            group = InetAddress.getByName(groupUrl);
            socket = new MulticastSocket(port);

            InetAddress inetAddress = discoveryUtils.addressList.get(9);
            System.out.println(inetAddress);
            NetworkInterface byInetAddress = NetworkInterface.getByInetAddress(inetAddress);
            socket.setNetworkInterface(byInetAddress);
//            socket.setNetworkInterface(NetworkInterface.getByInetAddress(InetAddress.getByName(localIp)));
            // Set the multicast datagram sending range to the local network
//            socket.setTimeToLive(1);
            // Set the maximum time for a socket to receive datagrams
            socket.setSoTimeout(SOCKET_TIMEOUT_MILL_SECONDS);
            // Join this multicast group
            socket.joinGroup(group);

        } catch (Exception e1) {
            // Join this multicast group
            socket.joinGroup(group);
            logger.error("createMulticastGroupAndJoin----->>>>Error: {}",  e1);
        }
        return socket;
    }
    private static void sendData(MulticastSocket socket, byte[] data,String groupUrl, int port) {
        try {
            InetAddress group = InetAddress.getByName(groupUrl);
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // Send data to the multicast group through the MulticastSocket instance port
            socket.send(packet);
            logger.info("------->>>>>Sending multicast packets in UDP");
        } catch (Exception e1) {
            logger.error("sendData------>>>>>Error: {}", e1);
        }
    }
    private static String receiveData(MulticastSocket socket,String groupUrl, int port) {
        String message;
        try {
            InetAddress group=InetAddress.getByName(groupUrl);
            byte[] data = new byte[1024 * 3];
            DatagramPacket packet = new DatagramPacket(data, data.length, group, port);
            // Receive data from the multicast group through the MulticastSocket instance port
            socket.receive(packet);
            message = new String(packet.getData(), 0, packet.getLength());
        } catch (Exception e) {
//             LOGGER.error("receiveData------>>>>>Error: {}", e);
            return "";
        }
        return message;
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void test() throws IOException {
        Random random = new Random();
        int port = random.nextInt(65535 - 1024 + 1) + 1024;

        String localIp = "127.0.0.1";
        SocketAddress socketAddress = new InetSocketAddress("192.168.30.221",port);

        final MulticastSocket socket = new MulticastSocket(socketAddress);//createMulticastGroupAndJoin(MULTICAST_IP, port, localIp);
        sendData(socket, SOAP_CONTENT.getBytes(), MULTICAST_IP, MULTICAST_PORT);

        while (true) {
            //Receive messages from multicast groups
            String message = receiveData(socket, MULTICAST_IP, MULTICAST_PORT);

            logger.info( message);
        }

    }
}
