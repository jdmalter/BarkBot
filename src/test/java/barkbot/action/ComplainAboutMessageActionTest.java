package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Mention;
import barkbot.model.Message;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ComplainAboutMessageActionTest {
    private ComplainAboutMessageAction subject;
    @Mock
    private PostToGroupMeClient postToGroupMeClient;
    @NonNull
    private String notifiedName;
    @NonNull
    private String notifiedUserId;


    @BeforeEach
    void setUp() {
        notifiedName = RandomPrimitiveFactory.createString();
        notifiedUserId = RandomPrimitiveFactory.createString();
        subject = new ComplainAboutMessageAction(postToGroupMeClient, notifiedName, notifiedUserId);
    }

    @Test
    void successfulCall() {
        final Message message = RandomMessageFactory.create();
        final String postText = String.format(ComplainAboutMessageAction.POST_TEXT_FORMAT, message.getName(), notifiedName);
        final Mention offender = Mention.builder()
                .userId(message.getUserId())
                .offset(ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@'))
                .length(1 + message.getName().length())
                .build();
        final int fromIndex = ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@') + 1 + message.getName().length();
        final Mention notified = Mention.builder()
                .userId(notifiedUserId)
                .offset(ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@', fromIndex))
                .length(1 + notifiedName.length())
                .build();

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(postText, offender, notified);
    }
}