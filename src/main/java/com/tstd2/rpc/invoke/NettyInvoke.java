package com.tstd2.rpc.invoke;

import com.tstd2.rpc.configBean.Reference;
import com.tstd2.rpc.loadbalance.LoadBalance;
import com.tstd2.rpc.loadbalance.NodeInfo;
import com.tstd2.rpc.netty.NettyUtil;
import com.tstd2.rpc.registry.RegistryNode;

import java.util.List;

public class NettyInvoke implements Invoke {

    @Override
    public Object invoke(Invocation invocation) throws Exception {

        List<RegistryNode> registryInfo = invocation.getReference().getRegistryInfo();

        // 负载均衡
        String loadbalance = invocation.getReference().getLoadbalance();
        Reference reference = invocation.getReference();
        LoadBalance loadBalanceBean = reference.getLoadBalances().get(loadbalance);

        NodeInfo nodeInfo = loadBalanceBean.deSelect(registryInfo);

        return NettyUtil.sendMsg(nodeInfo, invocation);
    }

}
