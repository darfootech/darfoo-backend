package com.darfoo.backend.cache;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by YY_410 on 2015/3/19.
 * 这个是监控redis cluster的22222端口
 */
public class MonitorRedis {

    static ObjectMapper objectMapper = new ObjectMapper();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss");

    public static void main(String[] args) {
        final MonitorRedis instance = new MonitorRedis();
        final SocketAddress addr = new InetSocketAddress("192.168.1.33", 22222);
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    Socket s = new Socket();
                    try {
                        s.connect(addr);
                        new Thread(instance.new ReadRunnable(s)).start();
                        Thread.sleep(300 * 1000l); //5min间隔请求redis cluster信息
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();

    }

    public void getClusterDetailInfo(String json) throws IOException {
        Map<String, Object> map = objectMapper.readValue(json, Map.class);
        Set<String> basicKeys = map.keySet();
        Map alphaMap = null;
        System.out.println("---basic start---");
        for (Iterator<String> basicIter = basicKeys.iterator(); basicIter.hasNext(); ) {
            String basicKey = basicIter.next();
            if (basicKey.equals("timestamp")) {
                System.out.println(basicKey + ":" + sdf.format(new Date((1000l * (Integer) map.get(basicKey)))));
            } else
                System.out.println(basicKey + ":" + map.get(basicKey));
            if (basicKey.equals("alpha")) {
                alphaMap = (Map) map.get("alpha");
            }
        }
        System.out.println("---basic end---");
        System.out.println("---cluster start---");
        Set<String> clusterKeys = alphaMap.keySet();
        Map<String, Object> serverMap = new HashMap<String, Object>();  //存redis server的情况，key为ip:port
        for (Iterator<String> clusterIter = clusterKeys.iterator(); clusterIter.hasNext(); ) {
            String clusterKey = clusterIter.next();
            System.out.println(clusterKey + ":" + alphaMap.get(clusterKey));
            if (clusterKey.matches("(\\d+\\.){3}\\d{1,3}:\\d{4}")) {  //简单的匹配一下
                //System.out.println("find:"+clusterKey+":"+alphaMap.get(clusterKey));
                serverMap.put(clusterKey, alphaMap.get(clusterKey));
            }
        }
        System.out.println("---cluster end---");
        Set<String> serverKeys = serverMap.keySet();
        for (Iterator<String> serverIter = serverKeys.iterator(); serverIter.hasNext(); ) {
            String serverKey = serverIter.next();
            System.out.println("[" + serverKey + "]");
            System.out.println(serverMap.get(serverKey));
        }
    }

    public class ReadRunnable implements Runnable {
        private Socket socket;

        ReadRunnable(Socket s) {
            this.socket = s;
        }

        @Override
        public void run() {
            InputStream is = null;
            try {
                is = socket.getInputStream();
                byte[] buffer = new byte[2048];
                int len;
                if ((len = is.read(buffer)) > 0) {
                    System.out.println(new String(buffer, 0, len));
                    getClusterDetailInfo(new String(buffer, 0, len));
                }
                Thread.sleep(1000l);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (socket != null)
                        socket.close();
                    is = null;
                    socket = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
