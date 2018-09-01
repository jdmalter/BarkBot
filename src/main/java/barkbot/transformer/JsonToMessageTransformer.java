package barkbot.transformer;

import barkbot.model.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;

@RequiredArgsConstructor
public class JsonToMessageTransformer {
    @NonNull
    private final ObjectMapper objectMapper;

    public Message convert(@NonNull final String json) {
        try {
            return objectMapper.readValue(json, Message.class);

        } catch (final JsonParseException e) {
            throw new IllegalArgumentException();

        } catch (final JsonMappingException e) {
            throw new AssertionError("bug in mapping code");

        } catch (final IOException e) {
            try {
                return objectMapper.readValue(json, Message.class);

            } catch (final IOException e1) {
                throw new UncheckedIOException("error in low-level I/O", e1);
            }
        }
    }
}
