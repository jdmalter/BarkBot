package barkbot.factory;

import barkbot.model.Upload;

public class UploadFactory {
    private static final int MAXIMUM_ATTACHMENTS_SIZE = 16;

    private UploadFactory() {
        throw new UnsupportedOperationException();
    }

    public static Upload create() {
        return Upload.builder()
                .attachments(RandomAttachmentFactory.createImmutableList(MAXIMUM_ATTACHMENTS_SIZE))
                .maxLabels(RandomPrimitiveFactory.createInt(Integer.MAX_VALUE))
                .minConfidence(RandomPrimitiveFactory.createFloat() * 100)
                .dimensionName(RandomPrimitiveFactory.createString())
                .outcome(RandomPrimitiveFactory.createString())
                .build();
    }
}
