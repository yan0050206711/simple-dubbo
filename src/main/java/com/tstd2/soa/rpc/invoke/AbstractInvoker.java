package com.tstd2.soa.rpc.invoke;

import com.tstd2.soa.common.LogIds;
import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.config.ProtocolConfig;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.support.RegistryLocalCache;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.rpc.loadbalance.LoadBalance;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class AbstractInvoker implements Invoker {

    @Override
    public Object invoke(Invocation invocation) throws Exception {

        RpcInvocation rpcInvocation = (RpcInvocation) invocation;
        ReferenceBean reference = rpcInvocation.getReference();

        // 取得注册中心中远程服务列表
        List<RegistryNode> registryInfo = RegistryLocalCache.getRegistry(reference.getInf());
        if (CollectionUtils.isEmpty(registryInfo)) {
            String msg = String.format("server not fount! serverName: %s", reference.getInf());
            throw new RuntimeException(msg);
        }

        // 负载均衡
        String loadbalance = reference.getLoadbalance();
        LoadBalance loadBalanceBean = ProtocolConfig.loadBalances.get(loadbalance);
        // 通过负载均衡策略选择一个节点
        RegistryNode registryNode = loadBalanceBean.deSelect(registryInfo);

        // 超时时间
        int timeout = this.getTimeout(registryNode, rpcInvocation);

        // 构造请求对象
        Request request = this.buildRequest(invocation);

        return this.doInvoke(registryNode.getProtocol(), request, timeout);
    }

    public int getTimeout(RegistryNode nodeInfo, RpcInvocation invocation) {
        // 默认用客户端配置的超时时间，客户端没有配置超时时间则用提供方的
        Integer timeout = Integer.parseInt(invocation.getReference().getTimeout());
        if (timeout == null || timeout == 0) {
            timeout = Integer.parseInt(nodeInfo.getService().getTimeout());
        }
        return timeout;
    }

    public Request buildRequest(Invocation invocation) {
        Request request = new Request();
        request.setSessionId(LogIds.generate());
        request.setClassName(invocation.getClassName());
        request.setMethodName(invocation.getMethodName());
        request.setParameterTypes(invocation.getParameterTypes());
        request.setArguments(invocation.getArguments());
        return request;
    }

    abstract Object doInvoke(ProtocolUrl protocolUrl, Request request, int timeout) throws Exception;
}
