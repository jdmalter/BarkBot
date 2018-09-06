package barkbot;

import barkbot.model.Request;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

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
    void handleRequest() {
        final BarkBot barkBot = Mockito.mock(BarkBot.class);
        final Request request = new Request();
        final Context context = Mockito.mock(Context.class);
        Mockito.when(applicationContext.getBean(BarkBot.class)).thenReturn(barkBot);

        subject.handleRequest(request, context);

        Mockito.verify(barkBot).accept(request.getBody());
    }
}