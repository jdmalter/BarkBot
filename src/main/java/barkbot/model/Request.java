package barkbot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request {
    private String body;
}
