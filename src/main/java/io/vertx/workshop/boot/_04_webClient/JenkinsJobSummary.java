package io.vertx.workshop.boot._04_webClient;

import io.vertx.core.json.JsonObject;

import java.util.Map;

public class JenkinsJobSummary extends JsonObject {

    public JenkinsJobSummary(Map<String, Object> map) {
        super(map);
    }

    public String name() {
        return getString("name");
    }
}
