package barkbot.rule;

import barkbot.client.DetectLabelsRekognitionClient;
import barkbot.model.Attachment;
import barkbot.model.Message;
import barkbot.transformer.UrlToByteBufferTransformer;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageContainsDogRule implements Rule {
    public static final String ACCEPTED_TYPE = "image";
    static final String ACCEPTED_LABEL = "Dog";

    @NonNull
    private final UrlToByteBufferTransformer urlToByteBufferTransformer;
    @NonNull
    private final DetectLabelsRekognitionClient detectLabelsRekognitionClient;

    @Override
    public boolean accepts(@NonNull final Message message) {
        return message.getAttachments()
                .stream()
                .filter(this::antecedent)
                .allMatch(this::consequent);
    }

    private boolean antecedent(final Attachment attachment) {
        return ACCEPTED_TYPE.equals(attachment.getType());
    }

    private boolean consequent(final Attachment attachment) {
        final Image image = new Image()
                .withBytes(urlToByteBufferTransformer.convert(attachment.getUrl()));
        return detectLabelsRekognitionClient.call(image)
                .stream()
                .map(Label::getName)
                .anyMatch(ACCEPTED_LABEL::equals);
    }
}
