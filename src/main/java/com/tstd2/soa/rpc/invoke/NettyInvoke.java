package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.Reference;
import com.tstd2.soa.registry.RegistryLocalCache;
import com.tstd2.soa.rpc.loadbalance.LoadBalance;
import com.tstd2.soa.remoting.netty.client.MessageSender;
import com.tstd2.soa.registry.RegistryNode;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class NettyInvoke implements Invoke {

    @Override
    public Object invoke(Invocation invocation) throws Exception {

        Reference reference = invocation.getReference();

        // 取得注册中心中远程服务列表
        List<RegistryNode> registryInfo = RegistryLocalCache.getRegistry(reference.getInf());
        if (CollectionUtils.isEmpty(registryInfo)) {
            String msg = String.format("server not fount! serverName: %s", reference.getInf());
            throw new RuntimeException(msg);
        }

        // 负载均衡
        String loadbalance = reference.getLoadbalance();
        LoadBalance loadBalanceBean = Reference.getLoadBalances().get(loadbalance);
        // 通过负载均衡策略选择一个节点
        RegistryNode nodeInfo = loadBalanceBean.deSelect(registryInfo);

        return MessageSender.send(nodeInfo, invocation);
    }

}
