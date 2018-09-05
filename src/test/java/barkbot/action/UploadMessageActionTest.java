package barkbot.action;

import barkbot.client.PutObjectS3Client;
import barkbot.factory.RandomMessageFactory;
import barkbot.factory.RandomPrimitiveFactory;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class UploadMessageActionTest {
    private UploadMessageAction subject;
    @Mock
    private UploadMessageAction.StringToFileWriter stringToFileWriter;
    @Mock
    private PutObjectS3Client putObjectS3Client;
    @NonNull
    private String messageBucket;
    private int maxLabels;
    private float minConfidence;

    @BeforeEach
    void setUp() {
        messageBucket = RandomPrimitiveFactory.createString();
        maxLabels = RandomPrimitiveFactory.createInt(Integer.MAX_VALUE);
        minConfidence = RandomPrimitiveFactory.createFloat() * 100;
        subject = new UploadMessageAction(stringToFileWriter, putObjectS3Client, messageBucket, maxLabels, minConfidence);
    }

    @Test
    void successfulCall() {
        final Message message = RandomMessageFactory.create();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        final String name = String.format(
                UploadMessageAction.FILE_NAME_FORMAT,
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                message.getId());
        final File file = new File(name);

        subject.execute(message);

        Mockito.verify(putObjectS3Client).call(messageBucket, file);
    }

    @Test
    void badIO() throws IOException {
        testException(IOException.class, UncheckedIOException.class);
    }

    @Test
    void badJVM() throws IOException {
        testException(UnsupportedEncodingException.class, AssertionError.class);
    }

    private void testException(final Class<? extends Throwable> throwable,
                               final Class<? extends Throwable> expected) throws IOException {
        final Message message = RandomMessageFactory.create();
        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        final String name = String.format(
                UploadMessageAction.FILE_NAME_FORMAT,
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                message.getId());
        final File file = new File(name);
        final String data = String.format(
                UploadMessageAction.DATA_FORMAT,
                message.getAttachments()
                        .stream()
                        .filter(attachment -> ImageContainsDogRule.ACCEPTED_TYPE.equals(attachment.getType()))
                        .map(Attachment::getUrl)
                        .map(url -> String.format("\"%s\"", url))
                        .collect(Collectors.joining(",")),
                maxLabels,
                minConfidence);
        Mockito.doThrow(throwable)
                .when(stringToFileWriter)
                .write(file, data, UploadMessageAction.CHARSET);

        Assertions.assertThrows(expected, () -> subject.execute(message));
    }
}