package com.tstd2.rpc.configBean;

import com.tstd2.rpc.netty.NettyClient;
import com.tstd2.rpc.netty.NettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class Protocol extends BaseConfigBean implements InitializingBean, ApplicationContextAware,
        ApplicationListener<ContextRefreshedEvent> {

    private static final long serialVersionUID = 7082032188443659845L;

    private static ApplicationContext applicationContext;

    private String name;
    private String port;
    private String host;
    private String contextpath;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextpath) {
        this.contextpath = contextpath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Protocol.applicationContext = applicationContext;
    }

    /**
     * spring启动完成以后触发的事件
     * @param event
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {
            return;
        }

        if ("netty".equals(name)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        NettyServer.start(port);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
