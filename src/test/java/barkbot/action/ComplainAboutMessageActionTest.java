package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.factory.RandomMessageFactory;
import barkbot.model.Mention;
import barkbot.model.Message;
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

    @BeforeEach
    void setUp() {
        subject = new ComplainAboutMessageAction(postToGroupMeClient);
    }

    @Test
    void successfulCall() {
        final Message message = RandomMessageFactory.create();
        final String postText = String.format(ComplainAboutMessageAction.POST_TEXT_FORMAT, message.getName());
        final Mention mention = Mention.builder()
                .userId(message.getUserId())
                .offset(ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@'))
                .length(1 + message.getName().length())
                .build();

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(postText, mention);
    }
}