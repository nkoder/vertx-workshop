package io.vertx.workshop.boot._04_webClient;

import io.vertx.core.json.JsonObject;

import java.util.Map;

public class JenkinsJob extends JsonObject {

    public JenkinsJob(String json) {
        super(json);
    }

    public JenkinsJob(Map<String, Object> map) {
        super(map);
    }

    public String name() {
        return getString("name");
    }

    public String color() {
        return getString("color");
    }
}
