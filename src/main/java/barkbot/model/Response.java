package barkbot.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Response {
    private boolean isBase64Encoded = false;
    private int statusCode;
    private Map<String, Object> headers = new HashMap<>();
    private String body = "{}";

    public static Response success() {
        final Response response = new Response();
        response.statusCode = 200;
        return response;
    }

    public static Response clientError() {
        final Response response = new Response();
        response.statusCode = 400;
        return response;
    }

    public static Response serverError() {
        final Response response = new Response();
        response.statusCode = 500;
        return response;
    }
}
