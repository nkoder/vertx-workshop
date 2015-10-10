package io.vertx.workshop.boot._06_sessionAndGlobalCounter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

import static java.lang.String.format;

public class Main_06_sessionAndGlobalCounter extends AbstractVerticle {

    private static final String CLIENT_URL = "http://localhost:8080";
    private static final String GLOBAL_COUNTER_URL = CLIENT_URL + "/counter/global";
    private static final String SESSION_COUNTER_URL = CLIENT_URL + "/counter/session";

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main_06_sessionAndGlobalCounter.class.getName());
    }

    private int counter = 1;

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(WebServer.class.getName());
        vertx.setPeriodic(2000L, event -> {
            vertx.eventBus().publish(WebServer.WEB_CLIENT_ADDRESS, "Hello " + counter);
            counter++;
        });
        System.out.println(format(
            "Make GET requests to %s to see how global counter increments across sessions (in different browsers etc.)",
            GLOBAL_COUNTER_URL
        ));
        System.out.println(format(
            "Make GET requests to %s to see how session counter increments independently in every session (in different browsers etc.)",
            SESSION_COUNTER_URL
        ));
    }

}
