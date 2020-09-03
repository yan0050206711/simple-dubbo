package com.tstd2.soa.xml.service;

import com.tstd2.soa.config.annotation.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String name) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello " + name;
    }
}
