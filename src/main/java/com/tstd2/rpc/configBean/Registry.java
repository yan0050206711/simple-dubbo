package com.tstd2.rpc.configBean;

import com.tstd2.rpc.registry.BaseRegistry;
import com.tstd2.rpc.registry.RedisRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class Registry extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

    private static final long serialVersionUID = 6931270359014167547L;

    private String protocol;

    private String address;

    private static Map<String, BaseRegistry> registryMap;

    private static ApplicationContext applicationContext;

    static {
        registryMap.put("redis", new RedisRegistry());
//        registryMap.put("zookeeper", new ZookeeperRegistry());
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Map<String, BaseRegistry> getRegistryMap() {
        return registryMap;
    }

    public static void setRegistryMap(Map<String, BaseRegistry> registryMap) {
        Registry.registryMap = registryMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Registry.applicationContext = applicationContext;
    }
}
