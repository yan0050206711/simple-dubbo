package com.tstd2.rpc.netty.model;

import java.io.Serializable;

/**
 * rpc服务应答结构
 */
public class Response implements Serializable {

    private String sessionId;
    private Object result;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}