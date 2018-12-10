package com.tstd2.soa.config;

import com.tstd2.soa.registry.BaseRegistryDelegate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Service extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

    private static final long serialVersionUID = -8551143436340555022L;

    private String inf;

    private String ref;

    private String protocol;

    private String timeout;

    private static ApplicationContext applicationContext;

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Service.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 将本service注册到注册中心
         */
        BaseRegistryDelegate.registry(inf, applicationContext);
    }
}
