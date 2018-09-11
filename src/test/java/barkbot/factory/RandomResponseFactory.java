package barkbot.factory;

import barkbot.model.Response;

public class RandomResponseFactory {
    private RandomResponseFactory() {
        throw new UnsupportedOperationException();
    }

    public static Response create() {
        return Response.builder()
                .statusCode(RandomPrimitiveFactory.createInt(Integer.MAX_VALUE))
                .build();
    }
}
