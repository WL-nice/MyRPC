package com.wanglei.myrpc.model;


import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务元信息（注册信息）
 */
@Data
public class ServiceMetaInfo {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";

    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 服务分组 目前没有实现该功能
     */
    private String serviceGroup = "default";

    /**
     * 服务域名
     */
    private String serviceHost;

    /**
     * 服务端口号
     */
    private Integer servicePort;


    /**
     * 获取服务键名
     *
     * @return
     */
    public String getServiceKey() {
        return serviceName + ":" + serviceVersion;
    }

    public String getServiceNodeKey() {
        return getServiceKey() + ":" + serviceAddress;
    }

    public String getServiceAddress() {
        if (!StrUtil.contains(serviceHost, "http")) {
            return "http://" + serviceHost + ":" + servicePort;
        }
        return serviceHost + ":" + servicePort;
    }

}
