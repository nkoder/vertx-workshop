package io.vertx.workshop.boot._04_webClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

import static java.lang.String.format;

public class JobFetcher extends AbstractVerticle {

    public static final String ADDRESS = "04::statusFetcher";

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer(ADDRESS, fetchStatusHandler());
        System.out.println(format("Starting %s verticle", JobFetcher.class.getSimpleName()));
    }

    private Handler<Message<JsonObject>> fetchStatusHandler() {
        return message -> {
            JenkinsJobSummary jobSummary = new JenkinsJobSummary(message.body().getMap());
            HttpClient httpClient = vertx.createHttpClient(httpClientOptionsForApacheBuilds());
            System.out.println(format("Fetching JSON of job %s...", jobSummary.name()));
            httpClient
                .get(format("/job/%s/api/json", jobSummary.name()))
                .handler(response -> {
                    if (response.statusCode() == 200) {
                        response.bodyHandler(bodyBuffer -> {
                            JsonObject job = new JenkinsJob(bodyBuffer.toString());
                            message.reply(job);
                        });
                    } else {
                        String failureMessage = format(
                            "Failed to fetch job '%s'. Status: %s",
                            jobSummary.name(),
                            response.statusCode());
                        message.fail(response.statusCode(), failureMessage);
                    }
                }).end();
        };
    }

    private HttpClientOptions httpClientOptionsForApacheBuilds() {
        return new HttpClientOptions()
            .setDefaultHost(WebClient.HOST)
            .setTrustAll(true)
            .setDefaultPort(443)
            .setSsl(true);
    }
}
