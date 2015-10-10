package io.vertx.workshop.boot._01_helloWorld;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import static java.lang.String.format;

public class Main_01_helloWorld extends AbstractVerticle {

    private static final String SOME_KEY = "someKey";

    private static Logger LOG = LoggerFactory.getLogger(Main_01_helloWorld.class);

    public static void main(String[] args) {
        LOG.info("main");
        Vertx.vertx().deployVerticle(Main_01_helloWorld.class.getName());
    }

    @Override
    public void start() throws Exception {
        System.out.println(format("Starting vert.x 3 application in thread %s", Thread.currentThread().getName()));
        System.out.println("Ready!");
    }

}
