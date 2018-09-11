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
    private final Message body;

    @JsonCreator
    public Request(@JsonProperty("body") @NonNull final Message body) {
        this.body = body;
    }
}
