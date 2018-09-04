package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.model.Mention;
import barkbot.model.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComplainAboutMessageAction implements Action {
    static final String POST_TEXT_FORMAT = "BARK BARK!! @%s";

    @NonNull
    private final PostToGroupMeClient postToGroupMeClient;

    @Override
    public void execute(@NonNull final Message message) {
        final String postText = String.format(POST_TEXT_FORMAT, message.getName());
        final Mention mention = Mention.builder()
                .userId(message.getUserId())
                .offset(POST_TEXT_FORMAT.indexOf('@'))
                .length(1 + message.getName().length())
                .build();
        postToGroupMeClient.call(postText, mention);
    }
}
