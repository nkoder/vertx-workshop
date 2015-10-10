package io.vertx.workshop.boot._05_serverRouting;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class RoutingWebServer extends AbstractVerticle {

    private final static String ROOT = "webroot";
    public static final String WEB_CLIENT_ADDRESS = "web.client";

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions bridgetOptions = new BridgeOptions().addOutboundPermitted(
            new PermittedOptions().setAddress(WEB_CLIENT_ADDRESS)
        );
        sockJSHandler.bridge(bridgetOptions);
        Router router = Router.router(vertx);
        router.route("/eventbus/*").handler(sockJSHandler);
        router.route().handler(FaviconHandler.create(ROOT + "/favicon.ico"));
        router.route().handler(StaticHandler.create(ROOT));
        server.requestHandler(router::accept).listen(8080);
    }
}
