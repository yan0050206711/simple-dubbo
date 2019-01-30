package com.tstd2.soa.remoting.exchange.model;

import java.io.Serializable;

/**
 * rpc服务应答结构
 */
public class Response implements Serializable {

    private String sessionId;
    private Object result;
    private String resultCode;
    private String errorMsg;

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

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}