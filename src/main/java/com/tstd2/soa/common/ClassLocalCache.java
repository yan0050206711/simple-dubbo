package com.tstd2.soa.common;

import java.util.WeakHashMap;

public class ClassLocalCache {

    private static WeakHashMap<String, Class<?>> weakHashMap = new WeakHashMap<>();

    public static void put(String className, Class<?> targetClass) {
        weakHashMap.put(className, targetClass);
    }

    public static Class<?> get(String className) {
        return weakHashMap.get(className);
    }

    public static Class<?> putAndGet(String className) throws ClassNotFoundException {
        Class<?> targetClass = get(className);
        if (targetClass == null) {
            synchronized (className.intern()) {
                targetClass = get(className);
                if (targetClass == null) {
                    targetClass = Class.forName(className);
                    put(className, targetClass);
                }
            }
        }
        return targetClass;
    }

}
