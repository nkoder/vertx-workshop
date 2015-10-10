package io.vertx.workshop.boot._06_sessionAndGlobalCounter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

import java.util.Optional;

public class WebServer extends AbstractVerticle {

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
        router.route().handler(FaviconHandler.create(ROOT + "/favicon.ico"));
        addGlobalCounterTo(router);
        addSessionCounterTo(router);
        router.route().handler(StaticHandler.create(ROOT));
        server.requestHandler(router::accept).listen(8080);
    }

    private void addGlobalCounterTo(Router router) {
        router.get("/counter/global").handler(context -> {
            LocalMap<Object, Object> localData = vertx.sharedData().getLocalMap("data");
            int counter = (int) Optional.ofNullable(localData.get("globalCounter")).orElse(0);
            counter++;
            localData.put("globalCounter", counter);
            context.response().end(String.valueOf(counter));
        });
    }

    private void addSessionCounterTo(Router router) {
        router.route().handler(CookieHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        router.get("/counter/session").handler(context -> {
            int counter = (int) Optional.ofNullable(context.session().get("sessionCounter")).orElse(0);
            counter++;
            context.session().put("sessionCounter", counter);
            context.response().end(String.valueOf(counter));
        });
    }
}
