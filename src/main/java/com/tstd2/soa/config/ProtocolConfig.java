package com.tstd2.soa.config;

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
import com.tstd2.soa.rpc.proxy.RpcProxy;
import com.tstd2.soa.rpc.proxy.javassist.JavassistProxy;
import com.tstd2.soa.rpc.proxy.jdk.JdkProxy;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yancey
 * @date 2021/1/30 22:34
 */
public class ProtocolConfig {

    /**
     * 调用者
     */
    public static Map<String, Invoker> invokes = new HashMap<>();

    /**
     * 负载策略
     */
    public static Map<String, LoadBalance> loadBalances = new HashMap<>();

    /**
     * 集群容错策略
     */
    public static Map<String, Cluster> clusters = new HashMap<>();

    /**
     * 动态代理接口
     */
    public static Map<String, RpcProxy> proxys = new HashMap<>();

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

}
