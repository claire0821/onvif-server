package com.root.onvif.util;

import org.junit.jupiter.api.Test;

import java.io.Console;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.*;

class DiscoveryUtilsTest {

    DiscoveryUtils discoveryUtils = new DiscoveryUtils();
    @Test
    void getLocalIP() {
        List<InetAddress> localIP = discoveryUtils.getLocalIP();
        System.out.println(localIP);
    }

    @Test
    void discovery() throws ExecutionException, InterruptedException {
        List<InetAddress> localIP = discoveryUtils.getLocalIP();
//        InetAddress address = localIP.get(4);
//        System.out.println(address);
        discoveryUtils.discovery(localIP.get(0),20);
        discoveryUtils.discovery(localIP.get(1),20);
        discoveryUtils.discovery(localIP.get(2),20);
        discoveryUtils.discovery(localIP.get(3),20);
        discoveryUtils.discovery(localIP.get(4),20);
//        Callable<Integer> call = new Callable<Integer>() {
//            @Override
//            public Integer call() throws Exception {
//                while (true) {
//                    discoveryUtils.discovery(localIP.get(0));
//                    System.out.println("正在计算结果..." + localIP.get(0));
//                    Thread.sleep(1000 * 1);
//                }
//            }
//        };
//
//        FutureTask<Integer> task = new FutureTask<>(call);
//        Thread thread = new Thread(task);
//        thread.start();
//
//        Integer state = null;
//        try {
//            state = task.get(5, TimeUnit.SECONDS);
//            System.out.println("state" + state);
//        }catch (TimeoutException e) {
//            e.printStackTrace();
//            System.out.println("time out");
//            task.cancel(true);
//        }




    }
}