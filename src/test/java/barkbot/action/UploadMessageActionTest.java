package barkbot.action;

import barkbot.client.PutObjectS3Client;
import barkbot.factory.RandomAttachmentFactory;
import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
class UploadMessageActionTest {
    private UploadMessageAction subject;
    @Mock
    private PutObjectS3Client putObjectS3Client;
    @NonNull
    private String messageBucket;
    private int maxLabels;
    private float minConfidence;
    @NonNull
    private String dimensionName;

    @BeforeEach
    void setUp() {
        messageBucket = RandomPrimitiveFactory.createString();
        maxLabels = RandomPrimitiveFactory.createInt(Integer.MAX_VALUE);
        minConfidence = RandomPrimitiveFactory.createFloat() * 100;
        dimensionName = RandomPrimitiveFactory.createString();
        subject = new UploadMessageAction(putObjectS3Client, messageBucket, maxLabels, minConfidence, dimensionName);
    }

    @Test
    void successfulCall() {
        final Message message = RandomMessageFactory.create(
                ImmutableList.of(
                        RandomAttachmentFactory.create(ImageContainsDogRule.ACCEPTED_TYPE)));
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        final String key = String.format(UploadMessageAction.FILE_NAME_FORMAT,
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                message.getId());

        subject.execute(message);

        Mockito.verify(putObjectS3Client).call(Mockito.eq(messageBucket), Mockito.eq(key), Mockito.any(InputStream.class));
    }

    @Test
    void badAttachments() {
        final Message message = RandomMessageFactory.create(ImmutableList.of());

        Assertions.assertThrows(IllegalArgumentException.class, () -> subject.execute(message));
    }
}