package com.simple.rpc.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: zhenghao
 * @Date: 2023/1/1
 */
public class IpUtil {

    /**
     * 获取本机内网ip
     */
    public static String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }


}
