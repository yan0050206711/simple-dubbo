package com.tstd2.soa.config;

import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.registry.BaseRegistryDelegate;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.support.RegistryLocalCache;
import com.tstd2.soa.rpc.invoke.Invoker;
import com.tstd2.soa.rpc.protocol.ProtocolFilterWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.List;

/**
 * 依赖服务配置
 */
public class ReferenceBean extends BaseConfigBean implements ApplicationContextAware, FactoryBean, InitializingBean {

    private static final long serialVersionUID = 8473037023470434275L;

    private String id;

    private String inf;

    private String loadbalance;

    private String protocol;

    private String cluster;

    private String retries;

    private String timeout;

    private String proxy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    /**
     * 返回一个交由Spring管理的实例，可以从Spring上下文拿到这个实例
     */
    @Override
    public Object getObject() throws Exception {
        Invoker invoker;
        if (protocol != null && !"".equals(protocol)) {
            invoker = ProtocolConfig.invokes.get(protocol);
        } else {
            ProtocolBean prot = SpringContextHolder.getBean(ProtocolBean.class);
            if (prot != null) {
                invoker = ProtocolConfig.invokes.get(prot.getName());
            } else {
                throw new RuntimeException("Protocol is null");
            }
        }

        invoker = new ProtocolFilterWrapper().refer(getObjectType(), invoker);

        // 生成一个代理对象
        return ProtocolConfig.proxys.get(proxy).getObject(inf, invoker, this);
    }

    /**
     * 返回实例的类型
     */
    @Override
    public Class<?> getObjectType() {
        try {
            if (inf != null && !"".equals(inf)) {
                return ReflectionCache.putAndGetClass(inf);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<RegistryNode> registryInfo = BaseRegistryDelegate.getRegistry(inf);
        RegistryLocalCache.setRegistry(inf, registryInfo);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setApplicationContext(applicationContext);
    }
}
