package io.vertx.workshop.boot._03_eventBus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

import static java.lang.String.format;

public class Main_03_eventBus extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main_03_eventBus.class.getName());
    }

    private int counter = 0;

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(PingVerticle.class.getName());
        vertx.deployVerticle(PongVerticle.class.getName(), new DeploymentOptions().setInstances(3));
        vertx.setPeriodic(1500, event -> {
            String ball = format("Hyper Ball Number %s", counter);
            System.out.println(String.format("Throw %s...", ball));
            vertx.eventBus().send(
                PingVerticle.ADDRESS,
                ball
            );
            counter++;
        });
    }

}
