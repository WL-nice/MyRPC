package com.wanglei.myrpc.config;

import com.wanglei.myrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * RPC框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "MyRPC";

    /**
     * 版本
     */
    private String version = "1.0";

    /**
     * 服务器主机名
     */
    private String serverHost = "localhost";

    /**
     * 服务器端口号
     */
    private Integer serverPort = 8070;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    private boolean mock = false;

    private RegistryConfig registryConfig = new RegistryConfig();

}
