package barkbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Upload {
    @NonNull
    private final ImmutableList<Attachment> attachments;
    @JsonProperty("max_labels")
    private final int maxLabels;
    @JsonProperty("min_confidence")
    private final float minConfidence;
    @NonNull
    @JsonProperty("dimension_name")
    private final String dimensionName;
    @NonNull
    private final String outcome;

    public Upload(@JsonProperty("attachments") @NonNull final List<Attachment> attachments,
                  @JsonProperty("max_labels") final int maxLabels,
                  @JsonProperty("min_confidence") final float minConfidence,
                  @JsonProperty("dimension_name") @NonNull final String dimensionName,
                  @JsonProperty("outcome") @NonNull final String outcome) {
        Preconditions.checkArgument(
                maxLabels >= 0,
                "maxLabels (%s) must be at least 0",
                maxLabels);
        Preconditions.checkArgument(
                minConfidence >= 0 && minConfidence <= 100,
                "minConfidence (%s) must be at least 0 and at most 100",
                minConfidence);

        this.attachments = ImmutableList.copyOf(attachments);
        this.maxLabels = maxLabels;
        this.minConfidence = minConfidence;
        this.dimensionName = dimensionName;
        this.outcome = outcome;
    }
}
