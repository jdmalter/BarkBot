package barkbot.client;

import barkbot.factory.RandomCollectionFactory;
import barkbot.factory.RandomLabelFactory;
import barkbot.factory.RandomPrimitiveFactory;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class DetectLabelsRekognitionClientTest {
    private static final int MAXIMUM_LABELS_SIZE = 16;

    private DetectLabelsRekognitionClient subject;
    @Mock
    private AmazonRekognition rekognition;
    private int maxLabels;
    private float minConfidence;
    private long retryTimeout;

    @BeforeEach
    void setUp() {
        maxLabels = RandomPrimitiveFactory.createInt(Integer.MAX_VALUE);
        minConfidence = RandomPrimitiveFactory.createFloat() * 100;
        // no sleeping during testing
        retryTimeout = 0;
        subject = new DetectLabelsRekognitionClient(rekognition, maxLabels, minConfidence, retryTimeout);
    }

    @Test
    void successfulCall() {
        final List<Label> expected = RandomCollectionFactory.create(
                RandomLabelFactory::create,
                RandomPrimitiveFactory.createInt(MAXIMUM_LABELS_SIZE));
        final Image image = new Image();
        final DetectLabelsRequest request = new DetectLabelsRequest()
                .withMaxLabels(maxLabels)
                .withMinConfidence(minConfidence)
                .withImage(image);
        final DetectLabelsResult result = new DetectLabelsResult().withLabels(expected);
        Mockito.when(rekognition.detectLabels(request)).thenReturn(result);

        final List<Label> actual = subject.call(image);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void successfulCallOnRetry() {
        final List<Label> expected = RandomCollectionFactory.create(
                RandomLabelFactory::create,
                RandomPrimitiveFactory.createInt(MAXIMUM_LABELS_SIZE));
        final Image image = new Image();
        final DetectLabelsRequest request = new DetectLabelsRequest()
                .withMaxLabels(maxLabels)
                .withMinConfidence(minConfidence)
                .withImage(image);
        final DetectLabelsResult result = new DetectLabelsResult().withLabels(expected);
        Mockito.when(rekognition.detectLabels(request))
                .thenThrow(InternalServerErrorException.class)
                .thenReturn(result);

        final List<Label> actual = subject.call(image);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badConfiguration() {
        testRekognitionException(InvalidParameterException.class, RuntimeException.class);
    }

    @Test
    void tooLargeImage() {
        testRekognitionException(ImageTooLargeException.class, IllegalArgumentException.class);
    }

    @Test
    void badCredentials() {
        testRekognitionException(AccessDeniedException.class, RuntimeException.class);
    }

    @Test
    void badImageFormat() {
        testRekognitionException(InvalidImageFormatException.class, IllegalArgumentException.class);
    }

    private void testRekognitionException(final Class<? extends Throwable> throwable,
                                          final Class<? extends Throwable> expected) {
        final Image image = new Image();
        final DetectLabelsRequest request = new DetectLabelsRequest()
                .withMaxLabels(maxLabels)
                .withMinConfidence(minConfidence)
                .withImage(image);
        Mockito.when(rekognition.detectLabels(request)).thenThrow(throwable);

        Assertions.assertThrows(expected, () -> subject.call(image));
    }
}