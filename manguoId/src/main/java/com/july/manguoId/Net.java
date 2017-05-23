package com.july.manguoId;

import com.google.common.primitives.Ints;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by haoyifen on 2017/5/22 17:45.
 * 用于获取本机ip的类
 * */
public class Net {
    public static int stringIpToInt(String ip) {
        try {
            byte[] address = InetAddress.getByName(ip).getAddress();
            return Ints.fromByteArray(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<String> getLocalIP() {
        List<String> ipList = new ArrayList<>();
        InetAddress ip;
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                // 遍历所有ip
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    String sIP = ip.getHostAddress();
                    if (sIP == null || sIP.contains(":")) {
                        continue;
                    }
                    ipList.add(sIP);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public static String firstNon127Or192() {
        List<String> ips = getLocalIP();
        for (String ip : ips) {
            if (!(ip.startsWith("127") || ip.startsWith("192"))) {
                return ip;
            }
        }
        return null;
    }

    public static String intToStringIp(int ip) {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getByAddress(Ints.toByteArray(ip)).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostAddress;
    }

}
