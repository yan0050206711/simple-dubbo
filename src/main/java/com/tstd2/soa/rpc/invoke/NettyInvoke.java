package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.Reference;
import com.tstd2.soa.rpc.loadbalance.LoadBalance;
import com.tstd2.soa.rpc.loadbalance.NodeInfo;
import com.tstd2.soa.remoting.netty.NettyClient;
import com.tstd2.soa.registry.RegistryNode;

import java.util.List;

public class NettyInvoke implements Invoke {

    @Override
    public Object invoke(Invocation invocation) throws Exception {

        List<RegistryNode> registryInfo = invocation.getReference().getRegistryInfo();

        // 负载均衡
        String loadbalance = invocation.getReference().getLoadbalance();
        LoadBalance loadBalanceBean = Reference.getLoadBalances().get(loadbalance);
        // 通过负载均衡策略选择一个节点
        NodeInfo nodeInfo = loadBalanceBean.deSelect(registryInfo);

        return NettyClient.sendMsg(nodeInfo, invocation);
    }

}
