package barkbot.client;

import barkbot.factory.RandomPrimitiveFactory;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;

@ExtendWith(MockitoExtension.class)
class PutObjectS3ClientTest {
    private PutObjectS3Client subject;
    @Mock
    private AmazonS3 s3;
    private long retryTimeout;

    @BeforeEach
    void setUp() {
        // no sleeping during testing
        retryTimeout = 0;
        subject = new PutObjectS3Client(s3, retryTimeout);
    }

    @Test
    void successfulCall() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream input = Mockito.mock(InputStream.class);

        subject.call(messageBucket, key, input);
    }

    @Test
    void successfulCallOnRetry() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream input = Mockito.mock(InputStream.class);
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Service);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class)))
                .thenThrow(serviceException)
                .thenReturn(new PutObjectResult());

        subject.call(messageBucket, key, input);

        Mockito.verify(s3, Mockito.times(2)).putObject(Mockito.any(PutObjectRequest.class));
    }

    @Test
    void badConfigurationForService() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream input = Mockito.mock(InputStream.class);
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Client);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, key, input));
    }

    @Test
    void badConfigurationForUnknown() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream input = Mockito.mock(InputStream.class);
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Unknown);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, key, input));
    }

    @Test
    void badConfigurationForClient() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream input = Mockito.mock(InputStream.class);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(SdkClientException.class);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, key, input));
    }
}