package com.tstd2.soa.registry.zookeeper;

import com.tstd2.soa.common.JsonUtils;
import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.config.Registry;
import com.tstd2.soa.config.Service;
import com.tstd2.soa.registry.BaseRegistry;
import com.tstd2.soa.registry.NotifyListener;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.registry.ServerNode;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * zk注册中心处理类
 */
public class ZookeeperRegistry implements BaseRegistry {

    private CuratorFramework zkClient;

    @Override
    public boolean registry(String interfaceName, ApplicationContext application) {
        try {
            Protocol protocol = application.getBean(Protocol.class);
            Service service = application.getBean("Service-" + interfaceName, Service.class);

            Registry registry = application.getBean(Registry.class);
            this.createZk(registry.getAddress());

            RegistryNode node = new RegistryNode();
            node.setProtocol(protocol);
            node.setService(service);

            // 更新zk
            this.sadd(interfaceName, node);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void unregister(String interfaceName, ServerNode serverfNode) {

    }

    @Override
    public void subscribe(ServerNode serverfNode, NotifyListener listener) {

    }

    @Override
    public void unsubscribe(ServerNode serverfNode, NotifyListener listener) {

    }

    @Override
    public List<RegistryNode> getRegistry(String interfaceName, ApplicationContext application) {
        try {
            Registry registry = application.getBean(Registry.class);
            this.createZk(registry.getAddress());

            String path = "/" + interfaceName;
            if (this.zkClient.checkExists().forPath(path) != null) {
                List<String> nodes = this.zkClient.getChildren().forPath(path);
                List<RegistryNode> nodeList = new ArrayList<>();
                for (String str : nodes) {
                    nodeList.add(JsonUtils.fromJson(str, RegistryNode.class));
                }
                return nodeList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化zk
     *
     * @param address
     */
    private synchronized void createZk(String address) {

        if (this.zkClient != null) {
            return;
        }

        this.zkClient = ZookeeperFactory.create(address);
    }

    private void sadd(String interfaceName, RegistryNode registryNode) throws Exception {

        String path = "/" + interfaceName;

        if (this.zkClient.checkExists().forPath(path) != null) {

            String host = registryNode.getProtocol().getHost();
            String port = registryNode.getProtocol().getPort();

            List<String> nodeList = this.zkClient.getChildren().forPath(path);

            for (String nodeJson : nodeList) {
                RegistryNode node = JsonUtils.fromJson(nodeJson, RegistryNode.class);

                // 是有有相同的机器，则删除后更新
                if (host.equals(node.getProtocol().getHost()) && port.equals(node.getProtocol().getPort())) {
                    // 存在则更新
                    this.zkClient.delete().forPath(path + "/" + nodeJson);
                    break;
                }
            }
        }

        this.zkClient.create().creatingParentsIfNeeded().forPath(path + "/" + JsonUtils.toJson(registryNode));
    }


}
