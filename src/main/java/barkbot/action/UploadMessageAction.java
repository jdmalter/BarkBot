package barkbot.action;

import barkbot.client.PutObjectS3Client;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.model.Upload;
import barkbot.rule.ImageContainsDogRule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@RequiredArgsConstructor
public class UploadMessageAction implements Action {
    static final String FILE_NAME_FORMAT = "%04d-%02d-%02d %02d:%02d:%02d %s";
    static final String DEFAULT_OUTCOME = "unknown";
    static final Charset CHARSET = Charset.defaultCharset();

    @NonNull
    private final PutObjectS3Client putObjectS3Client;
    @NonNull
    private final String messageBucket;
    private final int maxLabels;
    private final float minConfidence;
    @NonNull
    private final String dimensionName;

    @Override
    public void execute(@NonNull final Message message) {
        final ImmutableList<Attachment> acceptedAttachments = message.getAttachments()
                .stream()
                .filter(attachment -> ImageContainsDogRule.ACCEPTED_TYPE.equals(attachment.getType()))
                .collect(ImmutableList.toImmutableList());
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

        final Upload upload = Upload.builder()
                .attachments(acceptedAttachments)
                .maxLabels(maxLabels)
                .minConfidence(minConfidence)
                .dimensionName(dimensionName)
                .outcome(DEFAULT_OUTCOME)
                .build();

        final String data;
        try {
            data = new ObjectMapper().writeValueAsString(upload);

        } catch (final JsonProcessingException e) {
            throw new RuntimeException("bad json format");
        }
        final InputStream input = IOUtils.toInputStream(data, CHARSET);

        putObjectS3Client.call(messageBucket, key, input);
    }
}
