package com.tstd2.rpc.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CallBackHolder {

    private static Map<String, CallBack> callBackMap = new ConcurrentHashMap<>();

    public static void put(String key, CallBack callBack) {
        callBackMap.put(key, callBack);
    }

    public static CallBack get(String key) {
        return callBackMap.get(key);
    }

    public static void remove(String key) {
        callBackMap.remove(key);
    }
}
