package barkbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class Response {
    @JsonProperty("status_code")
    private final int statusCode;

    public Response(@JsonProperty("status_code") @NonNull final int statusCode) {
        Preconditions.checkArgument(
                statusCode >= 0,
                "statusCode (%s) must be at least 0",
                statusCode);

        this.statusCode = statusCode;
    }


    public static Response success() {
        return new Response(200);
    }

    public static Response clientError() {
        return new Response(400);
    }

    public static Response serverError() {
        return new Response(500);
    }
}
