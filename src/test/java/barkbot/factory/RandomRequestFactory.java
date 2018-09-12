package barkbot.factory;

import barkbot.model.Request;

public class RandomRequestFactory {
    private RandomRequestFactory() {
        throw new UnsupportedOperationException();
    }

    public static Request create() {
        return Request.builder()
                .body(RandomPrimitiveFactory.createString())
                .build();
    }
}
