package barkbot.model;

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
}
