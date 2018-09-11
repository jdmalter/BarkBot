package barkbot.factory;

import barkbot.model.Attachment;
import barkbot.model.Message;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;

public class RandomMessageFactory {
    private static final int MAXIMUM_ATTACHMENTS_SIZE = 16;

    private RandomMessageFactory() {
        throw new UnsupportedOperationException();
    }

    public static Message create() {
        return create(
                RandomAttachmentFactory.createImmutableList(
                        RandomPrimitiveFactory.createLong(MAXIMUM_ATTACHMENTS_SIZE)));
    }

    public static Message create(@NonNull final ImmutableList<Attachment> attachments) {
        return Message.builder()
                .attachments(attachments)
                .id(RandomPrimitiveFactory.createString())
                .name(RandomPrimitiveFactory.createString())
                .userId(RandomPrimitiveFactory.createString())
                .build();
    }
}
