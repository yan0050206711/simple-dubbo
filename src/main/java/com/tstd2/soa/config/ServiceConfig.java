package com.tstd2.soa.config;

import com.tstd2.soa.registry.BaseRegistryDelegate;

/**
 * @author yancey
 * @date 2020/8/23 22:23
 */
public class ServiceConfig {

    private String interfaceName;

    /**
     * 将服务发布
     */
    public void export() {
        /**
         * 将本service注册到注册中心
         */
        BaseRegistryDelegate.registry(getInterfaceName());
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
