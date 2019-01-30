package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.config.Reference;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.support.RegistryLocalCache;
import com.tstd2.soa.rpc.loadbalance.LoadBalance;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class AbstractInvoke implements Invoke {

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

        return this.invoke(nodeInfo, invocation);
    }

    abstract Object invoke(RegistryNode nodeInfo, Invocation invocation) throws Exception;
}
