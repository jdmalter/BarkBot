package barkbot.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Request {
    @NonNull
    private final Message body;
}
