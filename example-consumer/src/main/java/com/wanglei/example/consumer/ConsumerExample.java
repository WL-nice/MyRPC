package com.wanglei.example.consumer;

import com.wanglei.example.common.model.User;
import com.wanglei.example.common.service.UserService;
import com.wanglei.myrpc.proxy.ServiceProxyFactory;


import java.io.IOException;

public class ConsumerExample {
    public static void main(String[] args) throws IOException {
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("this is testName");
        User newUser =userService.getUser(user);
        if(newUser == null){
            System.out.println("user == null");
        }else{
            System.out.println(newUser.getName());
        }
//        long number = userService.getNumber();
//        System.out.println(number);

    }
}
