package com.wanglei.myrpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.wanglei.myrpc.RpcApplication;
import com.wanglei.myrpc.config.RpcConfig;
import com.wanglei.myrpc.constant.RpcConstant;
import com.wanglei.myrpc.model.RpcRequest;
import com.wanglei.myrpc.model.RpcResponse;
import com.wanglei.myrpc.model.ServiceMetaInfo;
import com.wanglei.myrpc.registry.Registry;
import com.wanglei.myrpc.registry.RegistryFactory;
import com.wanglei.myrpc.serializer.JdkSerializer;
import com.wanglei.myrpc.serializer.Serializer;
import com.wanglei.myrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务代理(JDK动态代理）
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化容器
        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //构造请求
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(serviceName)
                .methodName((method.getName()))
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();



        try {
            //序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            //获取配置信息
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            //通过配置信息的注册中心信息获取实例
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            //设置服务信息
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            //查询服务
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if(CollUtil.isEmpty(serviceMetaInfoList)){
                throw new RuntimeException("暂无服务地址");
            }

            ServiceMetaInfo selectServiceMetaInfo = serviceMetaInfoList.get(0);
            //发送请求
            try (HttpResponse httpResponse = HttpRequest.post(selectServiceMetaInfo.getServiceAddress())
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                //反序列化
                RpcResponse rpcResponse = serializer.deserializer(result, RpcResponse.class);
                return rpcResponse.getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
