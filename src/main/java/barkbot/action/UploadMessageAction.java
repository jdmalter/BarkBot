package barkbot.action;

import barkbot.client.PutObjectS3Client;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UploadMessageAction implements Action {
    static final String DATA_FORMAT = "{\"attachments\":[%s],\"maxLabels\":%d,\"minConfidence\":%f,\"outcome\":\"unknown\"}";
    static final String FILE_NAME_FORMAT = "%04d-%02d-%02d-%02d-%02d-%02d-%s";
    static final Charset CHARSET = Charset.defaultCharset();

    @NonNull
    private final PutObjectS3Client putObjectS3Client;
    @NonNull
    private final String messageBucket;
    private final int maxLabels;
    private final float minConfidence;

    @Override
    public void execute(@NonNull final Message message) {
        final List<Attachment> acceptedAttachments = message.getAttachments()
                .stream()
                .filter(attachment -> ImageContainsDogRule.ACCEPTED_TYPE.equals(attachment.getType()))
                .collect(Collectors.toList());
        Preconditions.checkArgument(!acceptedAttachments.isEmpty(),
                "attachments (%s) must contain at least one accepted type (%s)",
                acceptedAttachments,
                ImageContainsDogRule.ACCEPTED_TYPE);

        final LocalDate localDate = LocalDate.now();
        final LocalTime localTime = LocalTime.now();
        final String key = String.format(FILE_NAME_FORMAT,
                localDate.getYear(),
                localDate.getMonthValue(),
                localDate.getDayOfMonth(),
                localTime.getHour(),
                localTime.getMinute(),
                localTime.getSecond(),
                message.getId());

        final String data = String.format(DATA_FORMAT,
                acceptedAttachments.stream().map(Attachment::toJson).collect(Collectors.joining(",")),
                maxLabels,
                minConfidence);
        final InputStream input = IOUtils.toInputStream(data, CHARSET);

        putObjectS3Client.call(messageBucket, key, input);
    }
}
