package io.vertx.workshop.boot;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Random;

import static java.lang.String.format;

public class Starter extends AbstractVerticle {

    private static final String SOME_KEY = "someKey";

    private static Logger LOG = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        LOG.info("main");
        JsonObject config = new JsonObject().put(SOME_KEY, 123);
        Vertx.vertx().deployVerticle(
            Starter.class.getName(),
            new DeploymentOptions()
                .setInstances(5)
                .setConfig(config),
            result -> {
                if (result.succeeded()) {
                    System.out.println("Deployment done :-)"); // called once after all verticles deployed successfully
                } else {
                    System.out.println("Deployment failed :-( Cause is: " + result.cause()); // called for every verticle which failed to deploy
                }
            }
        );
    }

    @Override
    public void start() throws Exception {
        if (new Random().nextBoolean()) {
            throw new RuntimeException("some error here!");
        }
        System.out.println(format("Starting vert.x 3 application in thread %s", Thread.currentThread().getName()));
        System.out.println(String.format("Value of %s is %s", SOME_KEY, config().getValue(SOME_KEY)));
        System.out.println("Ready!");
    }

}
