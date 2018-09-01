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
    private final String avatarUrl;
    private final long createdAt;
    @NonNull
    private final String groupId;
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private final String senderId;
    @NonNull
    private final String senderType;
    @NonNull
    private final String sourceGuid;
    private final boolean system;
    @NonNull
    private final String text;
    @NonNull
    private final String userId;

    @JsonCreator
    public Message(@JsonProperty("attachments") @NonNull final List<Attachment> attachments,
                   @JsonProperty("avatar_url") @NonNull final String avatarUrl,
                   @JsonProperty("created_at") final long createdAt,
                   @JsonProperty("group_id") @NonNull final String groupId,
                   @JsonProperty("id") @NonNull final String id,
                   @JsonProperty("name") @NonNull final String name,
                   @JsonProperty("sender_id") @NonNull final String senderId,
                   @JsonProperty("sender_type") @NonNull final String senderType,
                   @JsonProperty("source_guid") @NonNull final String sourceGuid,
                   @JsonProperty("system") final boolean system,
                   @JsonProperty("text") @NonNull final String text,
                   @JsonProperty("user_id") @NonNull final String userId) {
        this.attachments = ImmutableList.copyOf(attachments);
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.groupId = groupId;
        this.id = id;
        this.name = name;
        this.senderId = senderId;
        this.senderType = senderType;
        this.sourceGuid = sourceGuid;
        this.system = system;
        this.text = text;
        this.userId = userId;
    }
}
