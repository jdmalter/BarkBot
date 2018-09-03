package barkbot.client;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.util.List;

public class DetectLabelsRekognitionClient {
    @NonNull
    private final AmazonRekognition rekognition;
    private final int maxLabels;
    private final float minConfidence;
    private final long retryTimeout;

    public DetectLabelsRekognitionClient(@NonNull final AmazonRekognition rekognition,
                                         final int maxLabels,
                                         final float minConfidence,
                                         final long retryTimeout) {
        Preconditions.checkArgument(
                maxLabels >= 0,
                "maxLabels (%s) must be at least 0",
                maxLabels);
        Preconditions.checkArgument(
                minConfidence >= 0 && minConfidence <= 100,
                "minConfidence (%s) must be at least 0 and at most 100",
                minConfidence);
        Preconditions.checkArgument(
                retryTimeout >= 0,
                "retryTimeout (%s) must be at least 0",
                retryTimeout);

        this.rekognition = rekognition;
        this.maxLabels = maxLabels;
        this.minConfidence = minConfidence;
        this.retryTimeout = retryTimeout;
    }

    public List<Label> call(@NonNull final Image image) {
        final DetectLabelsRequest request = new DetectLabelsRequest()
                .withMaxLabels(maxLabels)
                .withMinConfidence(minConfidence)
                .withImage(image);

        try {
            return safeCall(request);

        } catch (final InternalServerErrorException | ThrottlingException | ProvisionedThroughputExceededException e) {
            try {
                Thread.sleep(retryTimeout);
            } catch (final InterruptedException ignored) {
            }
            return safeCall(request);
        }
    }

    private List<Label> safeCall(final DetectLabelsRequest request) {
        try {
            return rekognition.detectLabels(request).getLabels();

        } catch (final InvalidParameterException e) {
            throw new RuntimeException("bug in configuration");

        } catch (final ImageTooLargeException e) {
            throw new IllegalArgumentException("image is too large");

        } catch (final AccessDeniedException e) {
            throw new RuntimeException("bad credentials");

        } catch (final InvalidImageFormatException e) {
            throw new IllegalArgumentException("bad image format");
        }
    }
}
