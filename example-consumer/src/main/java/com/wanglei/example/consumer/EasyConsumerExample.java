package com.wanglei.example.consumer;

import com.wanglei.example.common.model.User;
import com.wanglei.example.common.service.UserService;
import com.wanglei.myrpc.proxy.ServiceProxyFactory;

public class EasyConsumerExample {

    public static void main(String[] args) {
        //动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("heihei");

        User newUser =userService.getUser(user);
        if(newUser == null){
            System.out.println("user == null");
        }else{
            System.out.println(newUser.getName());
        }

    }
}
