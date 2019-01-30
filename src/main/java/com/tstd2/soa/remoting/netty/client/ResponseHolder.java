package com.tstd2.soa.remoting.netty.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseHolder {

    private static Map<String, ResponseFuture> responseMap = new ConcurrentHashMap<>();

    public static void put(String key, ResponseFuture future) {
        responseMap.put(key, future);
    }

    public static ResponseFuture get(String key) {
        return responseMap.get(key);
    }

    public static void remove(String key) {
        responseMap.remove(key);
    }
}
