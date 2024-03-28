package com.wanglei.myrpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务代理(JDK动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //根据方法返回类型，生成特定默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultObject(methodReturnType);
    }


    private Object getDefaultObject(Class<?> type){
        if(type.isPrimitive()){
            if(type == boolean.class){
                log.info("boolean");
                return false;
            }else if(type == short.class){
                log.info("short");
                return (short)0;
            } else if (type == int.class) {
                log.info("int");
                return 0;
            } else if (type == long.class) {
                log.info("long");
                return 0L;
            }
        }
        return null;
    }
}
