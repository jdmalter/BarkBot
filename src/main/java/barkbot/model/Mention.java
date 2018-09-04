package barkbot.model;

import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Mention {
    private final String userId;
    private final int offset;
    private final int length;

    public Mention(@NonNull final String userId, final int offset, final int length) {
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

    public String toAttachmentJson() {
        return String.format("{\"type\":\"mentions\",\"user_ids\":[%s],\"loci\":[[%d,%d]]}", userId, offset, length);
    }
}
