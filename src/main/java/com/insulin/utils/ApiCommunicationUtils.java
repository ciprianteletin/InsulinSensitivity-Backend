package com.insulin.utils;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ApiCommunicationUtils {

    public static HttpRequest obtainGetRequest(String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();
    }

    public static HttpRequest obtainPostClassificationRequest(String url, String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("content-type", "application/json")
                .build();
    }

    public static HttpResponse<String> getStringResponse(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

}
