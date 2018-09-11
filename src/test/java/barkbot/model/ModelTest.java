package barkbot.model;

import barkbot.factory.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ModelTest {
    @Test
    void randomAttachment() throws IOException {
        testRoundToRound(RandomAttachmentFactory.create(), Attachment.class);
    }

    @Test
    void randomMention() throws IOException {
        testRoundToRound(RandomMentionFactory.create(), Mention.class);
    }

    @Test
    void randomMessage() throws IOException {
        testRoundToRound(RandomMessageFactory.create(), Message.class);
    }

    @Test
    void randomRequest() throws IOException {
        testRoundToRound(RandomRequestFactory.create(), Request.class);
    }

    @Test
    void randomResponse() throws IOException {
        testRoundToRound(RandomResponseFactory.create(), Response.class);
    }

    @Test
    void randomUpload() throws IOException {
        testRoundToRound(UploadFactory.create(), Upload.class);
    }

    private <T> void testRoundToRound(final T expected, final Class<T> clazz) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();

        final String json = objectMapper.writeValueAsString(expected);
        final T actual = objectMapper.readValue(json, clazz);

        Assertions.assertEquals(expected, actual);
    }
}
