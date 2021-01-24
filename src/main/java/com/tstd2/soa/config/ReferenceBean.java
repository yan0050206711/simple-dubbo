package com.tstd2.soa.config;

import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.registry.BaseRegistryDelegate;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.support.RegistryLocalCache;
import com.tstd2.soa.rpc.cluster.Cluster;
import com.tstd2.soa.rpc.cluster.FailfastClusterInvoke;
import com.tstd2.soa.rpc.cluster.FailoverClusterInvoke;
import com.tstd2.soa.rpc.cluster.FailsafeClusterInvoke;
import com.tstd2.soa.rpc.invoke.HttpInvoker;
import com.tstd2.soa.rpc.invoke.Invoker;
import com.tstd2.soa.rpc.invoke.NettyInvoker;
import com.tstd2.soa.rpc.loadbalance.LoadBalance;
import com.tstd2.soa.rpc.loadbalance.RandomLoadBalance;
import com.tstd2.soa.rpc.loadbalance.RoundrobinLoadBalance;
import com.tstd2.soa.rpc.protocol.ProtocolFilterWrapper;
import com.tstd2.soa.rpc.proxy.RpcProxy;
import com.tstd2.soa.rpc.proxy.javassist.JavassistProxy;
import com.tstd2.soa.rpc.proxy.jdk.JdkProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 调用者
     */
    private static Map<String, Invoker> invokes = new HashMap<>();

    /**
     * 负载策略
     */
    private static Map<String, LoadBalance> loadBalances = new HashMap<>();

    /**
     * 集群容错策略
     */
    private static Map<String, Cluster> clusters = new HashMap<>();

    /**
     * 动态代理接口
     */
    private static Map<String, RpcProxy> proxys = new HashMap<>();

    static {
        invokes.put("netty", new NettyInvoker());
        invokes.put("http", new HttpInvoker());

        loadBalances.put("random", new RandomLoadBalance());
        loadBalances.put("roundrob", new RoundrobinLoadBalance());

        clusters.put("failover", new FailoverClusterInvoke());
        clusters.put("failfast", new FailfastClusterInvoke());
        clusters.put("failsafe", new FailsafeClusterInvoke());

        proxys.put("javassist", new JavassistProxy());
        proxys.put("jdk", new JdkProxy());
    }

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

    public static Map<String, Invoker> getInvokes() {
        return invokes;
    }

    public static void setInvokes(Map<String, Invoker> invokes) {
        ReferenceBean.invokes = invokes;
    }

    public static Map<String, LoadBalance> getLoadBalances() {
        return loadBalances;
    }

    public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
        ReferenceBean.loadBalances = loadBalances;
    }

    public static Map<String, Cluster> getClusters() {
        return clusters;
    }

    public static void setClusters(Map<String, Cluster> clusters) {
        ReferenceBean.clusters = clusters;
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
            invoker = invokes.get(protocol);
        } else {
            ProtocolBean prot = SpringContextHolder.getBean(ProtocolBean.class);
            if (prot != null) {
                invoker = invokes.get(prot.getName());
            } else {
                throw new RuntimeException("Protocol is null");
            }
        }

        invoker = new ProtocolFilterWrapper().refer(getObjectType(), invoker);

        // 生成一个代理对象
        return proxys.get(proxy).getObject(inf, invoker, this);
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
