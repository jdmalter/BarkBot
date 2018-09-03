package barkbot.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response {
    private boolean isBase64Encoded;
    private int statusCode;
    private Map<String, Object> headers;
    private String body;

    public static Response ok() {
        final Response response = new Response();
        response.isBase64Encoded = false;
        response.statusCode = 200;
        response.headers = new HashMap<>();
        response.body = "{}";
        return response;
    }
}
