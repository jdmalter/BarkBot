package barkbot.transformer;

import barkbot.model.Message;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.UncheckedIOException;

@Slf4j
@RequiredArgsConstructor
public class JsonToMessageTransformer {
    @NonNull
    private final ObjectMapper objectMapper;

    public Message convert(@NonNull final String json) {
        log.info("json={}", json);

        try {
            return objectMapper.readValue(json, Message.class);

        } catch (final JsonParseException | JsonMappingException e) {
            throw new IllegalArgumentException("bad json format");

        } catch (final IOException e) {
            try {
                return objectMapper.readValue(json, Message.class);

            } catch (final IOException e1) {
                throw new UncheckedIOException("error in low-level I/O on retry", e1);
            }
        }
    }
}
