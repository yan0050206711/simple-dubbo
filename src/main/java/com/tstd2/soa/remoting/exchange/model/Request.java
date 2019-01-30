package com.tstd2.soa.remoting.exchange.model;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = 3117006402387814342L;

    /**
     * 请求ID
     */
    private Long sessionId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parametersType;
    /**
     * 参数值
     */
    private Object[] parametersValue;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class<?>[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParametersValue() {
        return parametersValue;
    }

    public void setParametersValue(Object[] parametersValue) {
        this.parametersValue = parametersValue;
    }

}
