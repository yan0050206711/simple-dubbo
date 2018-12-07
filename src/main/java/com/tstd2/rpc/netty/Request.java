package com.tstd2.rpc.netty;

import java.io.Serializable;

public class Request implements Serializable {

    private static final long serialVersionUID = 3117006402387814342L;

    private String serviceId;
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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
