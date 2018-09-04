package barkbot.client;

import barkbot.factory.RandomMentionFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Mention;
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

    @BeforeEach
    void setUp() {
        subject = new PostToGroupMeClient(client, RandomPrimitiveFactory::createString);
    }

    @Test
    void successfulCall() throws IOException {
        final String text = RandomPrimitiveFactory.createString();
        final Mention mention = RandomMentionFactory.create();

        subject.call(text, mention);

        Mockito.verify(client).execute(Mockito.any(HttpPost.class));
    }

    @Test
    void throwIOException() throws IOException {
        final String text = RandomPrimitiveFactory.createString();
        final Mention mention = RandomMentionFactory.create();
        Mockito.doThrow(IOException.class).when(client).execute(Mockito.any(HttpPost.class));

        Assertions.assertThrows(UncheckedIOException.class, () -> subject.call(text, mention));
    }
}