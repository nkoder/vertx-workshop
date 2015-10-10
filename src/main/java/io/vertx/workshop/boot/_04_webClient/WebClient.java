package io.vertx.workshop.boot._04_webClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.stream.Collectors.toList;

public class WebClient extends AbstractVerticle {

    public static final String HOST = "jenkins.mono-project.com";

    @Override
    public void start() throws Exception {
        System.out.println(format("Starting %s verticle", WebClient.class.getSimpleName()));
        HttpClient httpClient = vertx.createHttpClient(httpClientOptionsForApacheBuilds());
        httpClient
            .get("/api/json")
            .handler(response -> {
                System.out.println("Got builds JSON");
                response.bodyHandler(bodyBuffer -> {
                    JsonObject responseJson = new JsonObject(bodyBuffer.toString());
                    JsonArray jobs = responseJson.getJsonArray("jobs");
                    List<CompletableFuture<JenkinsJob>> futures = jobs.stream()
                        .map(JsonObject.class::cast)
                        .map(job -> new JenkinsJobSummary(job.getMap()))
                        .map(jobSummary -> {
                            CompletableFuture<JenkinsJob> future = new CompletableFuture<>();
                            System.out.println(format(
                                "Delegating fetching JSON of job %s to %s vrticle...",
                                jobSummary.name(),
                                JobFetcher.class.getSimpleName()
                            ));
                            vertx.eventBus().send(JobFetcher.ADDRESS, jobSummary, handleFetchedJenkinsJobsUsing(future));
                            return future;
                        })
                        .collect(toList());
                    futures
                        .stream()
                        .forEach(future -> future.thenAccept(job ->
                                System.out.println(format("%s --> %s", job.name(), job.color()))
                        ));
                    allOf(futures.toArray(new CompletableFuture[futures.size()]))
                        .thenAccept(nothing -> System.out.println("DONE"));
                });
            })
            .end();
    }

    private Handler<AsyncResult<Message<JsonObject>>> handleFetchedJenkinsJobsUsing(final CompletableFuture<JenkinsJob> future) {
        return event -> {
            if (event.succeeded()) {
                JenkinsJob job = new JenkinsJob(event.result().body().getMap());
                future.complete(job);
            } else {
                future.complete(null);
            }
        };
    }

    private HttpClientOptions httpClientOptionsForApacheBuilds() {
        return new HttpClientOptions()
            .setDefaultHost(HOST)
            .setTrustAll(true)
            .setDefaultPort(443)
            .setSsl(true);
    }
}
