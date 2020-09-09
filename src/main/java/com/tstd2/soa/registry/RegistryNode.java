package com.tstd2.soa.registry;

public class RegistryNode {

    private ProtocolUrl protocol;
    private ServiceUrl service;

    public ProtocolUrl getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolUrl protocol) {
        this.protocol = protocol;
    }

    public ServiceUrl getService() {
        return service;
    }

    public void setService(ServiceUrl service) {
        this.service = service;
    }

    public static class ProtocolUrl {
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

    public static class ServiceUrl {
        private String inf;
        private String ref;
        private String protocol;
        private String timeout;

        public String getInf() {
            return inf;
        }

        public void setInf(String inf) {
            this.inf = inf;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getTimeout() {
            return timeout;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }
    }

}
