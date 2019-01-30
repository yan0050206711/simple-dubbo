package com.tstd2.soa.registry.support;

import com.tstd2.soa.registry.RegistryNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务注册列表本地缓存
 */
public class RegistryLocalCache {

    /**
     * 本地缓存
     */
    private static Map<String, List<RegistryNode>> registryLocalCache = new HashMap<>();

    /**
     * 缓存服务列表
     *
     * @param interfaceName
     * @param registryList
     */
    public synchronized static void setRegistry(final String interfaceName, List<RegistryNode> registryList) {
        registryLocalCache.put(interfaceName, registryList);
    }

    /**
     * 从本地缓存获取服务列表
     *
     * @param interfaceName
     * @return
     */
    public static List<RegistryNode> getRegistry(String interfaceName) {
        return registryLocalCache.get(interfaceName);
    }

}
