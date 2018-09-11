package barkbot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Message {
    @NonNull
    private final ImmutableList<Attachment> attachments;
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    @JsonProperty("user_id")
    private final String userId;

    @JsonCreator
    public Message(@JsonProperty("attachments") @NonNull final List<Attachment> attachments,
                   @JsonProperty("id") @NonNull final String id,
                   @JsonProperty("name") @NonNull final String name,
                   @JsonProperty("user_id") @NonNull final String userId) {
        this.attachments = ImmutableList.copyOf(attachments);
        this.id = id;
        this.name = name;
        this.userId = userId;
    }
}
