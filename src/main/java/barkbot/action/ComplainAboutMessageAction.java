package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.model.Mention;
import barkbot.model.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComplainAboutMessageAction implements Action {
    static final String POST_TEXT_FORMAT = "Hey @%s, that image doesn't look like a dog. Please stick to dog pictures! @%s, is this alright?!";

    @NonNull
    private final PostToGroupMeClient postToGroupMeClient;
    @NonNull
    private final String notifiedName;
    @NonNull
    private final String notifiedUserId;

    @Override
    public void execute(@NonNull final Message message) {
        final String postText = String.format(POST_TEXT_FORMAT, message.getName(), notifiedName);
        final Mention offender = Mention.builder()
                .userId(message.getUserId())
                .offset(POST_TEXT_FORMAT.indexOf('@'))
                .length(1 + message.getName().length())
                .build();
        final int fromIndex = POST_TEXT_FORMAT.indexOf('@') + message.getName().length() + 1;
        final Mention notified = Mention.builder()
                .userId(notifiedUserId)
                .offset(POST_TEXT_FORMAT.indexOf('@', fromIndex) + message.getName().length() - 2)
                .length(1 + notifiedName.length())
                .build();
        postToGroupMeClient.call(postText, offender, notified);
    }
}
