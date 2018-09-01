package barkbot.factory;

import barkbot.model.Attachment;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;

public class RandomAttachmentFactory {
    private RandomAttachmentFactory() {
        throw new UnsupportedOperationException();
    }

    public static Attachment create() {
        return create(RandomPrimitiveFactory.createString());
    }

    public static Attachment create(@NonNull final String type) {
        return Attachment.builder()
                .type(type)
                .url(RandomPrimitiveFactory.createString())
                .build();
    }

    public static ImmutableList<Attachment> createImmutableList(final long size) {
        return ImmutableList.copyOf(RandomCollectionFactory.create(RandomAttachmentFactory::create, size));
    }
}
