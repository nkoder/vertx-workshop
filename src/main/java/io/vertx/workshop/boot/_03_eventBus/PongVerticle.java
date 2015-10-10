package io.vertx.workshop.boot._03_eventBus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class PongVerticle extends AbstractVerticle {

    public static final String ADDRESS = "03::pong";

    @Override
    public void start() {
        vertx.eventBus().consumer(ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                String ball = message.body();
                vertx.setTimer(300, event -> {
                    System.out.println(String.format("pong %s (in %s)", ball, Thread.currentThread().getName()));
                    message.reply(ball);
                });
                vertx.setTimer(10000, event -> {
                    System.out.println(String.format("Undeploying %s", PongVerticle.class.getSimpleName()));
                    vertx.undeploy(deploymentID());
                });
            }
        });
    }
}
