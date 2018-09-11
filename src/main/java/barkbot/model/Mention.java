package barkbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Mention {
    @JsonProperty("user_id")
    private final String userId;
    private final int offset;
    private final int length;

    public Mention(@JsonProperty("user_id") @NonNull final String userId,
                   @JsonProperty("offset") final int offset,
                   @JsonProperty("length") final int length) {
        Preconditions.checkArgument(
                offset >= 0,
                "offset (%s) must be at least 0",
                offset);
        Preconditions.checkArgument(
                length >= 0,
                "length (%s) must be at least 0",
                length);

        this.userId = userId;
        this.offset = offset;
        this.length = length;
    }
}
