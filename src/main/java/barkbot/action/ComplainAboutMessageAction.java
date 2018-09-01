package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.model.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComplainAboutMessageAction implements Action {
    static final String POST_TEXT = "BARK BARK!!";

    @NonNull
    private final PostToGroupMeClient postToGroupMeClient;

    @Override
    public void execute(@NonNull final Message message) {
        postToGroupMeClient.call(POST_TEXT);
    }
}
