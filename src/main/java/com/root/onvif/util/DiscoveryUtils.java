package com.root.onvif.util;

import com.root.onvif.thread.AsyncService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sound.midi.Soundbank;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

@Component
public class DiscoveryUtils {

    @Autowired
    AsyncService asyncService;

    public static final String WS_DISCOVERY_PROBE_MESSAGE =
            "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:wsa=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" xmlns:tns=\"http://schemas.xmlsoap.org/ws/2005/04/discovery\"><soap:Header><wsa:Action>http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</wsa:Action><wsa:MessageID>urn:uuid:c032cfdd-c3ca-49dc-820e-ee6696ad63e2</wsa:MessageID><wsa:To>urn:schemas-xmlsoap-org:ws:2005:04:discovery</wsa:To></soap:Header><soap:Body><tns:Probe/></soap:Body></soap:Envelope>";
    public static final String WS_DISCOVERY_SOAP_VERSION = "SOAP 1.2 Protocol";
    public static final String WS_DISCOVERY_CONTENT_TYPE = "application/soap+xml";
    public static final int WS_DISCOVERY_TIMEOUT = 4000;
    public static final int WS_DISCOVERY_PORT = 3702;
    public static final String WS_DISCOVERY_ADDRESS_IPv4 = "239.255.255.250";
    private static final Random random = new SecureRandom();

    public List<InetAddress> addressList;//本地ip段

    public DiscoveryUtils() {
        addressList = new ArrayList<>();
    }
    public List<InetAddress> getLocalIP() {
        addressList = new ArrayList<>();
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface anInterface = interfaces.nextElement();
                    if (!anInterface.isLoopback()) {
                        final List<InterfaceAddress> interfaceAddresses = anInterface.getInterfaceAddresses();
                        for (InterfaceAddress address : interfaceAddresses) {
                            Class clz = address.getAddress().getClass();
                            if(address.getAddress() instanceof Inet4Address) {
                                addressList.add(address.getAddress());
                            }
//                            if (!enableIPv6 && address.getAddress() instanceof Inet6Address) continue;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } finally {
            return addressList;
        }
    }


    //根据ip段搜索设备 超时关闭搜索
    public List<Object> convertToIP(List<String> list) {
         List<Object> res = new ArrayList<Object>();//返回信息，第一个为onvif url，第二个为ip
        for(String datas: list) {
            String[] ip = xml(datas);
            asyncService.addSearchIP(ip);
//                        System.out.println(ip[0]);
            if(ip[0].length() > 0 && ip[1].length() > 0) {
                res.add(ip);
            }
        }
        return res;
    }
    public List<Object> discovery(InetAddress address, int second) {

        List<String> recvData = new ArrayList<String>();

        final String uuid = UUID.randomUUID().toString();
        final String probe =
                WS_DISCOVERY_PROBE_MESSAGE.replaceAll(
                        "<wsa:MessageID>urn:uuid:.*</wsa:MessageID>",
                        "<wsa:MessageID>urn:uuid:" + uuid + "</wsa:MessageID>");
        final int port = random.nextInt(20000) + 40000;
//        DatagramSocket server = null;
        MulticastSocket server = null;
        try {
            SocketAddress socketAddress = new InetSocketAddress(address.getHostAddress(),port);

            server = new MulticastSocket(socketAddress);//new DatagramSocket(port, address);
            server.setBroadcast(true);
//            server.setTTL((byte) 100);
            server.send(
                    new DatagramPacket(
                            probe.getBytes(StandardCharsets.UTF_8),
                            probe.length(),
                            InetAddress.getByName(WS_DISCOVERY_ADDRESS_IPv4),
                            WS_DISCOVERY_PORT));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MulticastSocket finalServer = server;
//        DatagramSocket finalServer = server;
        Callable<Integer> call2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                while (true) {
                    final DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
                    try {
                        finalServer.receive(packet);
                        System.out.println("地址" + packet.getAddress());
                        byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
                        recvData.add(new String(datas));
//                        System.out.println(new String(datas));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        FutureTask<Integer> task = new FutureTask<>(call2);
        Thread thread = new Thread(task);
        thread.start();

        Integer state2 = null;
        try {
            try {
                state2 = task.get(second, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("state" + state2);
        }catch (TimeoutException e) {
            e.printStackTrace();
            System.out.println("time out");
            task.cancel(true);
        } finally {
            List<Object> objects = convertToIP(recvData);//返回信息，第一个为onvif url，第二个为ip
            return objects;
        }
//        while (true) {
//            final DatagramPacket packet = new DatagramPacket(new byte[4096], 4096);
//            try {
//                server.receive(packet);
//                byte[] datas = Arrays.copyOf(packet.getData(), packet.getLength());
//                xml(new String(datas));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private String[] xml(String str) {
        Document xmlDoc;
        try {
            xmlDoc = DocumentHelper.parseText(str);
            Element root = xmlDoc.getRootElement();
            Element body = null;
            for (Iterator i = root.elementIterator(); i.hasNext();) {
                Element el = (Element) i.next();
                if(el.getName().equals("Body")) {
                    body = el;
                    break;
                }
            }

            if(body != null) {
                List<Element> childElements = body.elements();
                Map<String,Object> mapEle = new HashMap<String, Object>();
                //遍历子节点
                mapEle = getAllElements(childElements,mapEle);
//                System.out.println(mapEle);
                String[] ip = getDeviceIP(mapEle.get("XAddrs").toString());
//                System.out.println(ip);
                return ip;
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
    private final static String HTTP = "http://";
    private String[] getDeviceIP(String str) {
        String [] array = new String[2];
        //10.20.30.99/onvif/device_service http://[fe80::a614:37ff:fe81:d221]/onvif/device_service 去除后面的
        array[0] = str;//onvif url
        int index = str.indexOf(" ");
        if(index != -1)  {
            array[0] = str.substring(0,index);//onvif url
        }

        index = -1;
        index = str.indexOf("onvif");
        if(index >= 0) {
            int indexStart = str.indexOf(HTTP);
            str = str.substring(indexStart + HTTP.length());
            int indexEnd = str.indexOf("/");
//            str = str.substring(0,indexEnd);
//            indexEnd = str.indexOf(":");
            if(indexEnd > 0) {
                str = str.substring(0,indexEnd);
                array[1] = str;//ip
            } else {
                indexEnd = str.indexOf("/");
                if(indexEnd > 0) {
                    str = str.substring(0,indexEnd);
                    array[1] = str;//ip
                }
            }
        } else {
            str = "";
            array[1] = str;
        }
        return array;
    }

    private Map<String, Object> getAllElements(List<Element> childElements,Map<String,Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if(ele.elements().size()>0){
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }
    private static Collection<Node> getNodeMatching(Node body, String regexp) {
        final Collection<Node> nodes = new ArrayList<>();
        if (body.getNodeName().matches(regexp)) nodes.add(body);
        if (body.getChildNodes().getLength() == 0) return nodes;
        NodeList returnList = body.getChildNodes();
        for (int k = 0; k < returnList.getLength(); k++) {
            final Node node = returnList.item(k);
            nodes.addAll(getNodeMatching(node, regexp));
        }
        return nodes;
    }
}
