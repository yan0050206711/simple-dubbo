package com.tstd2.soa.registry;

import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.common.ServiceUrl;
import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.config.RegistryBean;
import com.tstd2.soa.config.ServiceBean;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.registry.support.RegistryListener;
import org.springframework.beans.BeanUtils;

import java.util.List;

public class BaseRegistryDelegate {

    public static void registry(String interfaceName) {
        ProtocolBean protocolBean = SpringContextHolder.getBean(ProtocolBean.class);
        ServiceBean serviceBean = SpringContextHolder.getBean("Service-" + interfaceName, ServiceBean.class);

        RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);

        RegistryNode node = new RegistryNode();
        ProtocolUrl protocolUrl = new ProtocolUrl();
        BeanUtils.copyProperties(protocolBean, protocolUrl);
        ServiceUrl serviceUrl = new ServiceUrl();
        BeanUtils.copyProperties(serviceBean, serviceUrl);
        node.setProtocol(protocolUrl);
        node.setService(serviceUrl);

        registryBean.registry(interfaceName, node);
    }

    public static List<RegistryNode> getRegistry(String interfaceName) {
        RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);

        // 获取注册列表
        List<RegistryNode> registryNodeList = registryBean.getRegistry(interfaceName);
        // 注册监听
        registryBean.subscribe(interfaceName, new RegistryListener(interfaceName));

        return registryNodeList;
    }

}
