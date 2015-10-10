package io.vertx.workshop.boot._03_eventBus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;

public class PingVerticle extends AbstractVerticle {

    public static final String ADDRESS = "03::ping";

    @Override
    public void start() {
        vertx.eventBus().consumer(ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(Message<String> message) {
                String ball = message.body();
                vertx.setTimer(300, event1 -> {
                    System.out.println(String.format("ping %s (in %s)", ball, Thread.currentThread().getName()));
                    vertx.eventBus().send(PongVerticle.ADDRESS, ball, replyHandler());
                });
            }
        });
    }

    private Handler<AsyncResult<Message<String>>> replyHandler() {
        return event -> {
            if (event.succeeded()) {
                String receivedBall = event.result().body();
                System.out.println(String.format("maybe ping again %s ? Nope :-)", receivedBall));
            } else {
                System.out.println("Something failed...");
                event.cause().printStackTrace();
                System.out.println(String.format("Undeploying %s", PingVerticle.class.getSimpleName()));
                vertx.undeploy(deploymentID());
            }
        };
    }
}
