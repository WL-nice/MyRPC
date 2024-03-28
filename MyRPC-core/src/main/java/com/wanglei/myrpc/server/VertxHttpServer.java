package com.wanglei.myrpc.server;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

/**
 * Vertx HTTP服务器
 */
@Slf4j
public class VertxHttpServer implements HttpServer {
    public void doStart(int port) {
        //创建Vertx实例
        Vertx vertx = Vertx.vertx();

        //创建HTTP服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        //监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        //启动HTTP服务并监听端口
        server.listen(port, result -> {
            if (result.succeeded()) {
                log.info("Server is now listening on port: " + port);
            } else {
                log.info("Failed to start server " + result.cause());
            }
        });

    }
}
