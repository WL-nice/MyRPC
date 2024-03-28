package com.wanglei.example.provider;

import com.wanglei.myrpc.RpcApplication;
import com.wanglei.myrpc.registry.LocalRegistry;
import com.wanglei.myrpc.server.HttpServer;
import com.wanglei.myrpc.server.VertxHttpServer;

public class ExampleProvider {
    public static void main(String[] args) {
        RpcApplication.init();

        LocalRegistry.registry(UserServiceImpl.class.getName(),UserServiceImpl.class);

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
