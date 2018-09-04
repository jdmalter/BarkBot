package barkbot.factory;

import barkbot.model.Mention;

public class RandomMentionFactory {
    private RandomMentionFactory() {
        throw new UnsupportedOperationException();
    }

    public static Mention create() {
        return Mention.builder()
                .userId(RandomPrimitiveFactory.createString())
                .offset(RandomPrimitiveFactory.createInt(Integer.MAX_VALUE))
                .length(RandomPrimitiveFactory.createInt(Integer.MAX_VALUE))
                .build();
    }
}
