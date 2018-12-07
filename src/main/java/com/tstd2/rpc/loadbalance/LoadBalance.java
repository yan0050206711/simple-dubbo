package com.tstd2.rpc.loadbalance;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalance {

    NodeInfo deSelect(List<String> registryInfo);

}
