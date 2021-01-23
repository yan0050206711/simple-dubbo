package com.tstd2.soa.common;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionCache {

    private static Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();
    private static Map<String, Method> METHOD_CACHE = new ConcurrentHashMap<>();
    private static Map<String, MethodAccess> METHOD_ACCESS_CACHE = new ConcurrentHashMap<>();
    private static Map<String, Integer> METHOD_INDEX_CACHE = new ConcurrentHashMap<>();

    public static void putClass(String className, Class<?> targetClass) {
        CLASS_CACHE.put(className, targetClass);
    }

    public static Class<?> getClass(String className) {
        return CLASS_CACHE.get(className);
    }

    public static Class<?> putAndGetClass(String className) throws ClassNotFoundException {
        Class<?> targetClass = getClass(className);
        if (targetClass == null) {
            synchronized (className.intern()) {
                targetClass = getClass(className);
                if (targetClass == null) {
                    targetClass = Class.forName(className);
                    putClass(className, targetClass);
                }
            }
        }
        return targetClass;
    }

    public static void putMethod(Class<?> classType, Class<?>[] parametersType, Method method) {
        METHOD_CACHE.put(join(classType, method.getName(), parametersType), method);
    }

    public static Method getMethod(Class<?> classType, String methodName, Class<?>[] parametersType) {
        return METHOD_CACHE.get(join(classType, methodName, parametersType));
    }

    public static Method putAndGetMethod(Class<?> classType, String methodName, Class<?>[] parametersType) throws NoSuchMethodException {
        Method method = getMethod(classType, methodName, parametersType);
        if (method == null) {
            synchronized (classType) {
                method = getMethod(classType, methodName, parametersType);
                if (method == null) {
                    method = classType.getMethod(methodName, parametersType);
                    putMethod(classType, parametersType, method);
                }
            }
        }
        return method;
    }

    private static String join(Class<?> classType, String methodName, Class<?>[] parametersType) {
        StringJoiner joiner = new StringJoiner("-");
        joiner.add(classType.getName()).add(methodName);
        for (Class<?> type : parametersType) {
            joiner.add(type.getName());
        }
        return joiner.toString();
    }

    public static void putMethodAccess(Class<?> classType, MethodAccess methodAccess) {
        METHOD_ACCESS_CACHE.put(classType.getName(), methodAccess);
    }

    public static MethodAccess getMethodAccess(Class<?> classType) {
        return METHOD_ACCESS_CACHE.get(classType.getName());
    }

    public static MethodAccess putAndGetMethodAccess(Class<?> classType) {
        MethodAccess methodAccess = getMethodAccess(classType);
        if (methodAccess == null) {
            synchronized (classType) {
                methodAccess = getMethodAccess(classType);
                if (methodAccess == null) {
                    methodAccess = MethodAccess.get(classType);
                    putMethodAccess(classType, methodAccess);
                }
            }
        }
        return methodAccess;
    }

    public static void putMethodIndex(Class<?> classType, String methodName, Class<?>[] parametersType, Integer index) {
        METHOD_INDEX_CACHE.put(join(classType, methodName, parametersType), index);
    }

    public static Integer getMethodIndex(Class<?> classType, String methodName, Class<?>[] parametersType) {
        return METHOD_INDEX_CACHE.get(join(classType, methodName, parametersType));
    }

    public static Integer putAndGetMethodIndex(Class<?> classType, String methodName, Class<?>[] parametersType, MethodAccess methodAccess) {
        Integer index = getMethodIndex(classType, methodName, parametersType);
        if (index == null) {
            synchronized (classType) {
                index = getMethodIndex(classType, methodName, parametersType);
                if (index == null) {
                    index = methodAccess.getIndex(methodName, parametersType);
                    putMethodIndex(classType, methodName, parametersType, index);
                }
            }
        }
        return index;
    }

}
