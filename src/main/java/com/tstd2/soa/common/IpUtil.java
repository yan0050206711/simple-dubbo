package com.tstd2.soa.common;

import java.net.InetAddress;

/**
 * @author yancey
 * @date 2018/12/8 19:57
 */
public class IpUtil {

    /**
     * 获取本机ip
     * @return
     * @throws Exception
     */
    public static String getLocalHost() throws Exception {
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        return hostAddress;
    }

}
