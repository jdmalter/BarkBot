package barkbot.provider;

import barkbot.action.ComplainAboutMessageAction;
import barkbot.action.UploadMessageAction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ClientProvider.class})
@Configuration
@RequiredArgsConstructor
public class ActionProvider {
    @NonNull
    private final ClientProvider clientProvider;
    @Value("${NOTIFIED_NAME}")
    private String notifiedName;
    @Value("${NOTIFIED_USER_ID}")
    private String notifiedUserId;
    @Value("${MESSAGE_BUCKET}")
    private String messageBucket;
    @Value("${MAX_LABELS}")
    private int maxLabels;
    @Value("${MIN_CONFIDENCE}")
    private float minConfidence;
    @Value("${DIMENSION_NAME}")
    private String dimensionName;

    @Bean
    public ComplainAboutMessageAction complainAboutMessageAction() {
        return new ComplainAboutMessageAction(clientProvider.postToGroupMeClient(), notifiedName, notifiedUserId);
    }

    @Bean
    public UploadMessageAction uploadMessageAction() {
        return new UploadMessageAction(clientProvider.putObjectS3Client(), messageBucket, maxLabels, minConfidence, dimensionName);
    }
}
