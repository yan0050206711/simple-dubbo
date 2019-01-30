package com.tstd2.soa.remoting;

public enum ErrorCode {

    SUCCESS("1"), ERROR("2");

    public String errorCode;

    ErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
