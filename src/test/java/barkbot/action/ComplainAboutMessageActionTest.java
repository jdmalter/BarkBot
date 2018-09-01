package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.factory.RandomMessageFactory;
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

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(ComplainAboutMessageAction.POST_TEXT);
    }
}