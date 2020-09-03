package com.tstd2.soa.remoting.exchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseHolder {

    private static Map<Long, DefaultFuture> responseMap = new ConcurrentHashMap<>();

    public static void put(Long key, DefaultFuture future) {
        responseMap.put(key, future);
    }

    public static DefaultFuture get(Long key) {
        return responseMap.get(key);
    }

    public static DefaultFuture remove(Long key) {
        return responseMap.remove(key);
    }
}
