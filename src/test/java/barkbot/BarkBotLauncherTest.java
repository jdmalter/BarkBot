package barkbot;

import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomRequestFactory;
import barkbot.model.Message;
import barkbot.model.Request;
import barkbot.model.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.UncheckedIOException;

@ExtendWith(MockitoExtension.class)
class BarkBotLauncherTest {
    private BarkBotLauncher subject;
    @Mock
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        subject = new BarkBotLauncher(applicationContext);
    }

    @Test
    void handleRequest() throws IOException {
        final Request request = RandomRequestFactory.create();
        final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        final Message message = RandomMessageFactory.create();
        final BarkBot barkBot = Mockito.mock(BarkBot.class);
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(Mockito.<Class<Object>>any()))
                .thenReturn(objectMapper)
                .thenReturn(barkBot);
        Mockito.when(objectMapper.readValue(request.getBody(), Message.class)).thenReturn(message);

        subject.handleRequest(request, context);

        Mockito.verify(barkBot).accept(message);
    }

    @Test
    void badJson() throws IOException {
        final Response expected = Response.clientError();
        final Request request = RandomRequestFactory.create();
        final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(Mockito.<Class<Object>>any())).thenReturn(objectMapper);
        Mockito.when(objectMapper.readValue(request.getBody(), Message.class)).thenThrow(JsonParseException.class);

        final Response actual = subject.handleRequest(request, context);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badIO() throws IOException {
        final Response expected = Response.serverError();
        final Request request = RandomRequestFactory.create();
        final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(Mockito.<Class<Object>>any())).thenReturn(objectMapper);
        Mockito.when(objectMapper.readValue(request.getBody(), Message.class)).thenThrow(IOException.class);

        final Response actual = subject.handleRequest(request, context);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badArgument() throws IOException {
        final Response expected = Response.clientError();
        final Request request = RandomRequestFactory.create();
        final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        final Message message = RandomMessageFactory.create();
        final BarkBot barkBot = Mockito.mock(BarkBot.class);
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(Mockito.<Class<Object>>any()))
                .thenReturn(objectMapper)
                .thenReturn(barkBot);
        Mockito.when(objectMapper.readValue(request.getBody(), Message.class)).thenReturn(message);
        Mockito.doThrow(IllegalArgumentException.class).when(barkBot).accept(message);

        final Response actual = subject.handleRequest(request, context);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badBarkBot() throws IOException {
        final Response expected = Response.serverError();
        final Request request = RandomRequestFactory.create();
        final ObjectMapper objectMapper = Mockito.mock(ObjectMapper.class);
        final Message message = RandomMessageFactory.create();
        final BarkBot barkBot = Mockito.mock(BarkBot.class);
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(Mockito.<Class<Object>>any()))
                .thenReturn(objectMapper)
                .thenReturn(barkBot);
        Mockito.when(objectMapper.readValue(request.getBody(), Message.class)).thenReturn(message);
        Mockito.doThrow(UncheckedIOException.class).when(barkBot).accept(message);

        final Response actual = subject.handleRequest(request, context);

        Assertions.assertEquals(expected, actual);
    }
}