package barkbot.action;

import barkbot.client.PostToGroupMeClient;
import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Mention;
import barkbot.model.Message;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        final int fromIndex = ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@') + message.getName().length() + 1;
        final Mention notified = Mention.builder()
                .userId(notifiedUserId)
                .offset(ComplainAboutMessageAction.POST_TEXT_FORMAT.indexOf('@', fromIndex) + message.getName().length() - 2)
                .length(1 + notifiedName.length())
                .build();

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(postText, offender, notified);
    }

    @Test
    void verifyOffenderName() {
        final Message message = RandomMessageFactory.create();
        final String expected = "@" + message.getName();
        final ArgumentCaptor<String> postTextCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Mention> offenderCaptor = ArgumentCaptor.forClass(Mention.class);

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(postTextCaptor.capture(), offenderCaptor.capture(), Mockito.any(Mention.class));
        final String postText = postTextCaptor.getValue();
        final Mention offender = offenderCaptor.getValue();

        final String actual = postText.substring(offender.getOffset(), offender.getOffset() + offender.getLength());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void verifyNotifiedName() {
        final Message message = RandomMessageFactory.create();
        final String expected = "@" + notifiedName;
        final ArgumentCaptor<String> postTextCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<Mention> notifiedCaptor = ArgumentCaptor.forClass(Mention.class);

        subject.execute(message);

        Mockito.verify(postToGroupMeClient).call(postTextCaptor.capture(), Mockito.any(Mention.class), notifiedCaptor.capture());
        final String postText = postTextCaptor.getValue();
        final Mention notified = notifiedCaptor.getValue();

        final String actual = postText.substring(notified.getOffset(), notified.getOffset() + notified.getLength());
        Assertions.assertEquals(expected, actual);
    }
}