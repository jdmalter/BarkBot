package barkbot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Request {
    @NonNull
    private final String body;

    @JsonCreator
    public Request(@JsonProperty("body") @NonNull final String body) {
        this.body = body;
    }
}
