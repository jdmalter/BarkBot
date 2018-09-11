package barkbot;

import barkbot.action.ComplainAboutMessageAction;
import barkbot.action.UploadMessageAction;
import barkbot.factory.RandomMessageFactory;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BarkBotTest {
    private BarkBot subject;
    @Mock
    private ImageContainsDogRule imageContainsDogRule;
    @Mock
    private ComplainAboutMessageAction complainAboutMessageAction;
    @Mock
    private UploadMessageAction uploadMessageAction;

    @BeforeEach
    void setUp() {
        subject = new BarkBot(imageContainsDogRule, complainAboutMessageAction, uploadMessageAction);
    }

    @Test
    void ruleAccepts() {
        final Message message = RandomMessageFactory.create();
        Mockito.when(imageContainsDogRule.accepts(message)).thenReturn(true);

        subject.accept(message);

        Mockito.verify(complainAboutMessageAction, Mockito.never()).execute(message);
        Mockito.verify(uploadMessageAction, Mockito.never()).execute(message);
    }

    @Test
    void ruleRejects() {
        final Message message = RandomMessageFactory.create();
        Mockito.when(imageContainsDogRule.accepts(message)).thenReturn(false);

        subject.accept(message);

        Mockito.verify(complainAboutMessageAction).execute(message);
        Mockito.verify(uploadMessageAction).execute(message);
    }
}