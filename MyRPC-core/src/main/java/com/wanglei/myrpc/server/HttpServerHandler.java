package com.wanglei.myrpc.server;

import com.wanglei.myrpc.RpcApplication;
import com.wanglei.myrpc.model.RpcRequest;
import com.wanglei.myrpc.model.RpcResponse;
import com.wanglei.myrpc.registry.LocalRegistry;
import com.wanglei.myrpc.serializer.JdkSerializer;
import com.wanglei.myrpc.serializer.Serializer;
import com.wanglei.myrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP请求处理
 */
@Slf4j
public class HttpServerHandler implements Handler<HttpServerRequest> {

    //反序列化请求为对象，并从请求对象中获取参数
    //根据服务名称从本地注册器中获取到对应的服务实现类
    //通过反射机制调用方法，得到返回结果
    //对返回结果进行封装和序列化，并写入到响应中

    @Override
    public void handle(HttpServerRequest request) {
        //指定序列化容器
        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        //记录日志
        log.info("接受请求:" + request.method() + " " + request.uri());

        //异步处理HTTP请求
        request.bodyHandler(body->{
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try{
                rpcRequest = serializer.deserializer(bytes,RpcRequest.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            //构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            //如果请求为null 直接返回
            if(rpcRequest == null){
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request,rpcResponse,serializer);
                return;
            }

            try{
                //获取要调用的服务实现类，通过反射调用
                Class<?> impClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = impClass.getMethod(rpcRequest.getMethodName(),rpcRequest.getParameterTypes());
                Object result = method.invoke(impClass.getDeclaredConstructor().newInstance(),rpcRequest.getArgs());
                //封装返回结果
                rpcResponse.setData(result);
                rpcResponse.setDateType(method.getReturnType());
                rpcResponse.setMessage("ok");
            }catch (Exception e){
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            //响应
            doResponse(request,rpcResponse,serializer);
        });
    }

    /**
     * 响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request,RpcResponse rpcResponse,Serializer serializer){
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type","application/json");
        try{
            //序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        }catch (IOException e){
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}
