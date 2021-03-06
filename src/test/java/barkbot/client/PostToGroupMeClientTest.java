package barkbot.client;

import barkbot.factory.RandomMentionFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Mention;
import lombok.NonNull;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.UncheckedIOException;

@ExtendWith(MockitoExtension.class)
class PostToGroupMeClientTest {
    private PostToGroupMeClient subject;
    @Mock
    private HttpClient client;
    @NonNull
    private String botId;

    @BeforeEach
    void setUp() {
        botId = RandomPrimitiveFactory.createString();
        subject = new PostToGroupMeClient(client, botId);
    }

    @Test
    void successfulCall() throws IOException {
        final String text = RandomPrimitiveFactory.createString();
        final Mention offender = RandomMentionFactory.create();
        final Mention notified = RandomMentionFactory.create();

        subject.call(text, offender, notified);

        Mockito.verify(client).execute(Mockito.any(HttpPost.class));
    }

    @Test
    void badIO() throws IOException {
        final String text = RandomPrimitiveFactory.createString();
        final Mention offender = RandomMentionFactory.create();
        final Mention notified = RandomMentionFactory.create();
        Mockito.doThrow(IOException.class).when(client).execute(Mockito.any(HttpPost.class));

        Assertions.assertThrows(UncheckedIOException.class, () -> subject.call(text, offender, notified));
    }
}