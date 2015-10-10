package io.vertx.workshop.boot._05_serverRouting;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import static java.lang.String.format;

public class Main_05_serverRouting extends AbstractVerticle {

    private static final String CLIENT_URL = "http://localhost:8080/eventbus.html";

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main_05_serverRouting.class.getName());
    }

    private int counter = 1;

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(RoutingWebServer.class.getName());
        vertx.setPeriodic(2000L, event -> {
            vertx.eventBus().publish(RoutingWebServer.WEB_CLIENT_ADDRESS, "Hello " + counter);
            counter++;
        });
        System.out.println(format(
            "Open %s to see how frontend receives messages from backend :-)", CLIENT_URL
        ));
    }

}
