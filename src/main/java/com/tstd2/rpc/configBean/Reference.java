package com.tstd2.rpc.configBean;

import com.tstd2.rpc.cluster.Cluster;
import com.tstd2.rpc.cluster.FailfastClusterInvoke;
import com.tstd2.rpc.cluster.FailoverClusterInvoke;
import com.tstd2.rpc.cluster.FailsafeClusterInvoke;
import com.tstd2.rpc.invoke.Invoke;
import com.tstd2.rpc.invoke.NettyInvoke;
import com.tstd2.rpc.loadbalance.LoadBalance;
import com.tstd2.rpc.loadbalance.RandomLoadBalance;
import com.tstd2.rpc.loadbalance.RoundrobLoadBalance;
import com.tstd2.rpc.proxy.advice.InvokeInvocationHandler;
import com.tstd2.rpc.registry.BaseRegistryDelegate;
import com.tstd2.rpc.registry.RegistryNode;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reference extends BaseConfigBean implements FactoryBean, InitializingBean, ApplicationContextAware {


    private static final long serialVersionUID = 8473037023470434275L;

    private String id;

    private String inf;

    private String loadbalance;

    private String protocol;

    private String cluster;

    private String retries;

    private static ApplicationContext applicationContext;

    private Invoke invoke;

    private static Map<String, Invoke> invokes = new HashMap<>();

    private static Map<String, LoadBalance> loadBalances = new HashMap<>();

    private static Map<String, Cluster> clusters = new HashMap<>();

    /**
     * 生产者的多个服务的列表
     */
    private List<RegistryNode> registryInfo = new ArrayList<>();

    static {
        invokes.put("netty", new NettyInvoke());
//        invokes.put("mina", new MineInvoke());

        loadBalances.put("random", new RandomLoadBalance());
        loadBalances.put("roundrob", new RoundrobLoadBalance());

        clusters.put("failover", new FailoverClusterInvoke());
        clusters.put("failfast", new FailfastClusterInvoke());
        clusters.put("failsafe", new FailsafeClusterInvoke());
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

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Invoke getInvoke() {
        return invoke;
    }

    public void setInvoke(Invoke invoke) {
        this.invoke = invoke;
    }

    public static Map<String, Invoke> getInvokes() {
        return invokes;
    }

    public static void setInvokes(Map<String, Invoke> invokes) {
        Reference.invokes = invokes;
    }

    public static Map<String, LoadBalance> getLoadBalances() {
        return loadBalances;
    }

    public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
        Reference.loadBalances = loadBalances;
    }

    public static Map<String, Cluster> getClusters() {
        return clusters;
    }

    public static void setClusters(Map<String, Cluster> clusters) {
        Reference.clusters = clusters;
    }

    public List<RegistryNode> getRegistryInfo() {
        return registryInfo;
    }

    public void setRegistryInfo(List<RegistryNode> registryInfo) {
        this.registryInfo = registryInfo;
    }

    /**
     * 返回一个交由Spring管理的实例，可以从Spring上下文拿到这个实例
     */
    @Override
    public Object getObject() throws Exception {
        if (protocol != null && !"".equals(protocol)) {
            invoke = invokes.get(protocol);
        } else {
            Protocol prot = applicationContext.getBean(Protocol.class);
            if (prot != null) {
                invoke = invokes.get(prot.getName());
            } else {
                throw new RuntimeException("Protocol is null");
            }
        }

        // 生成一个代理对象
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class<?>[]{Class.forName(inf)},
                new InvokeInvocationHandler(invoke, this));
    }

    /**
     * 返回实例的类型
     */
    @Override
    public Class<?> getObjectType() {
        try {
            if (inf != null && !"".equals(inf)) {
                return Class.forName(inf);
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
        registryInfo = BaseRegistryDelegate.getRegistry(inf, applicationContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Reference.applicationContext = applicationContext;
    }
}
