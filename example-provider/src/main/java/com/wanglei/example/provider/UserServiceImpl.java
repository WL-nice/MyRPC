package com.wanglei.example.provider;

import com.wanglei.example.common.model.User;
import com.wanglei.example.common.service.UserService;

public class UserServiceImpl implements UserService {
   public User getUser(User user){
       System.out.println("用户名为： " + user.getName());
        return user;
   }
}
