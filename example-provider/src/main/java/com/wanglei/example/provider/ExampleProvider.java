package com.wanglei.example.provider;

import com.wanglei.example.common.service.UserService;
import com.wanglei.myrpc.RpcApplication;
import com.wanglei.myrpc.config.RegistryConfig;
import com.wanglei.myrpc.config.RpcConfig;
import com.wanglei.myrpc.model.ServiceMetaInfo;
import com.wanglei.myrpc.registry.LocalRegistry;
import com.wanglei.myrpc.registry.Registry;
import com.wanglei.myrpc.registry.RegistryFactory;
import com.wanglei.myrpc.server.HttpServer;
import com.wanglei.myrpc.server.VertxHttpServer;

public class ExampleProvider {
    public static void main(String[] args) {
        RpcApplication.init();

        //第一个为UserService 第二个为UserServiceImpl 也就是实现类，这样调用UserService的方法会找实现类的方法
//        LocalRegistry.registry(UserService.class.getName(), UserServiceImpl.class);

        String serviceName = UserService.class.getName();
        LocalRegistry.registry(serviceName,UserServiceImpl.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
        serviceMetaInfo.setServiceAddress(rpcConfig.getServerHost()+":"+rpcConfig.getServerPort());
        try{
            registry.register(serviceMetaInfo);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        HttpServer httpServer = new VertxHttpServer();
        httpServer.doStart(RpcApplication.getRpcConfig().getServerPort());
    }
}
