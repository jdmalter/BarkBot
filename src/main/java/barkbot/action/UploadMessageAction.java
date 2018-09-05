package barkbot.action;

import barkbot.client.PutObjectS3Client;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UploadMessageAction implements Action {
    static final String DATA_FORMAT = "{\"urls\":[%s],\"maxLabels\":%d,\"minConfidence\":%f,\"outcome\":\"unknown\"}";
    static final String FILE_NAME_FORMAT = "%04d-%02d-%02d-%02d-%02d-%02d-%s";
    static final Charset CHARSET = Charset.defaultCharset();

    @NonNull
    private final StringToFileWriter stringToFileWriter;
    @NonNull
    private final PutObjectS3Client putObjectS3Client;
    @NonNull
    private final String messageBucket;
    private final int maxLabels;
    private final float minConfidence;

    @Override
    public void execute(@NonNull final Message message) {
        final String data = String.format(
                DATA_FORMAT,
                message.getAttachments()
                        .stream()
                        .filter(attachment -> ImageContainsDogRule.ACCEPTED_TYPE.equals(attachment.getType()))
                        .map(Attachment::getUrl)
                        .map(url -> String.format("\"%s\"", url))
                        .collect(Collectors.joining(",")),
                maxLabels,
                minConfidence);

        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        final String name = String.format(
                FILE_NAME_FORMAT,
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                message.getId());
        final File file = new File(name);

        try {
            stringToFileWriter.write(file, data, CHARSET);

        } catch (final UnsupportedEncodingException e) {
            throw new AssertionError("bug in JVM; buy lottery tickets");

        } catch (final IOException e) {
            throw new UncheckedIOException("error in low-level I/O", e);
        }

        putObjectS3Client.call(messageBucket, file);
    }

    public interface StringToFileWriter {
        void write(File file, String string, Charset charset) throws UnsupportedOperationException, IOException;
    }
}
