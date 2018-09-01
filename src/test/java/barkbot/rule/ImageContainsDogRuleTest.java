package barkbot.rule;

import barkbot.client.LabelRekognitionClient;
import barkbot.factory.RandomAttachmentFactory;
import barkbot.factory.RandomMessageFactory;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.transformer.UrlToByteBufferTransformer;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;

@ExtendWith(MockitoExtension.class)
class ImageContainsDogRuleTest {
    private ImageContainsDogRule subject;
    @Mock
    private UrlToByteBufferTransformer urlToByteBufferTransformer;
    @Mock
    private LabelRekognitionClient labelRekognitionClient;

    @BeforeEach
    void setUp() {
        subject = new ImageContainsDogRule(urlToByteBufferTransformer, labelRekognitionClient);
    }

    @Test
    void noAttachments() {
        final Message message = createMessage();

        Assertions.assertTrue(subject.accepts(message));
    }

    @Test
    void noImageAttachments() {
        final Message message = createMessage(RandomAttachmentFactory.create(""));

        Assertions.assertTrue(subject.accepts(message));
    }

    @Test
    void noDogLabels() {
        final String string = "http:///foo";
        final ByteBuffer byteBuffer = Mockito.mock(ByteBuffer.class);
        final Message message = createMessage(new Attachment(ImageContainsDogRule.ACCEPTED_TYPE, string));
        final Image image = new Image().withBytes(byteBuffer);
        Mockito.when(urlToByteBufferTransformer.convert(string)).thenReturn(byteBuffer);
        Mockito.when(labelRekognitionClient.call(image)).thenReturn(Lists.newArrayList());

        Assertions.assertFalse(subject.accepts(message));
    }

    @Test
    void someDogLabels() {
        final String string = "http:///foo";
        final ByteBuffer byteBuffer = Mockito.mock(ByteBuffer.class);
        final Message message = createMessage(new Attachment(ImageContainsDogRule.ACCEPTED_TYPE, string));
        final Image image = new Image().withBytes(byteBuffer);
        Mockito.when(urlToByteBufferTransformer.convert(string)).thenReturn(byteBuffer);
        Mockito.when(labelRekognitionClient.call(image))
                .thenReturn(Lists.newArrayList(new Label().withName(ImageContainsDogRule.ACCEPTED_LABEL)));

        Assertions.assertTrue(subject.accepts(message));
    }

    private Message createMessage(final Attachment... attachments) {
        return RandomMessageFactory.create(ImmutableList.copyOf(attachments));
    }
}