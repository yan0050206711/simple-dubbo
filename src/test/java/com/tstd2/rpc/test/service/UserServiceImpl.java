package com.tstd2.rpc.test.service;

public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String name) {
        return "hello " + name;
    }
}
