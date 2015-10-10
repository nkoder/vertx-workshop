package io.vertx.workshop.boot._04_webClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main_04_webClient extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(Main_04_webClient.class.getName());
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(JobFetcher.class.getName(), new DeploymentOptions().setInstances(5));
        vertx.deployVerticle(WebClient.class.getName());
    }

}
