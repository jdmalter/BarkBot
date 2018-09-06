package barkbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Attachment {
    @NonNull
    private final String type;
    @NonNull
    private final String url;

    public Attachment(@JsonProperty("type") @NonNull final String type,
                      @JsonProperty("url") @NonNull final String url) {
        this.type = type;
        this.url = url;
    }

    public String toJson() {
        return String.format("{\"type\":\"%s\",\"url\":\"%s\"}", type, url);
    }
}
