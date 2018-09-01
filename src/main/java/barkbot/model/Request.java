package barkbot.model;

import lombok.Data;

import java.util.Map;

@Data
public class Request {
    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, Object> headers;
    private Map<String, Object> queryStringParameters;
    private Map<String, Object> pathParameters;
    private Map<String, Object> stageVariables;
    private Map<String, Object> requestContext;
    private String body;
    private boolean isBase64Encoded;
}
