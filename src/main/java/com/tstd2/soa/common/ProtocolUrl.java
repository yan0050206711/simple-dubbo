package com.tstd2.soa.common;

/**
 * 注册协议URL
 */
public class ProtocolUrl {
    private String name;
    private String port;
    private String host;
    private String contextpath;
    private String threads;
    private String serialize;

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

    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }
}