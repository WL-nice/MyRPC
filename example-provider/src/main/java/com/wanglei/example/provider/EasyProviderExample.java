package com.wanglei.example.provider;

import com.wanglei.example.common.service.UserService;
import com.wanglei.myrpc.registry.LocalRegistry;
import com.wanglei.myrpc.server.HttpServer;
import com.wanglei.myrpc.server.VertxHttpServer;

public class EasyProviderExample {
    public static void main(String[] args) {
        //注册服务
        LocalRegistry.registry(UserService.class.getName(), UserServiceImpl.class);
        //提供服务
        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(8070);

    }
}
