package barkbot.transformer;

import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;

@ExtendWith(MockitoExtension.class)
class JsonToMessageTransformerTest {
    private JsonToMessageTransformer subject;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        subject = new JsonToMessageTransformer(objectMapper);
    }

    @Test
    void successfulConvert() throws IOException {
        final Message expected = RandomMessageFactory.create();
        final String json = RandomPrimitiveFactory.createString();
        Mockito.when(objectMapper.readValue(json, Message.class)).thenReturn(expected);

        final Message actual = subject.convert(json);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void successfulConvertOnRetry() throws IOException {
        final Message expected = RandomMessageFactory.create();
        final String json = RandomPrimitiveFactory.createString();
        Mockito.when(objectMapper.readValue(json, Message.class))
                .thenThrow(IOException.class)
                .thenReturn(expected);

        final Message actual = subject.convert(json);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badJson() throws IOException {
        testObjectMapperException(JsonParseException.class, IllegalArgumentException.class);
    }

    @Test
    void badMapping() throws IOException {
        testObjectMapperException(JsonMappingException.class, AssertionError.class);
    }

    @Test
    void badIO() throws IOException {
        testObjectMapperException(IOException.class, UncheckedIOException.class);
    }

    private void testObjectMapperException(final Class<? extends Throwable> throwable,
                                           final Class<? extends Throwable> expected) throws IOException {
        final String json = RandomPrimitiveFactory.createString();
        Mockito.when(objectMapper.readValue(json, Message.class)).thenThrow(throwable);

        Assertions.assertThrows(expected, () -> subject.convert(json));
    }
}