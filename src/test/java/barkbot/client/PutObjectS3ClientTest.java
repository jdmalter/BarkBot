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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

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
        final ArgumentCaptor<PutObjectRequest> argument = ArgumentCaptor.forClass(PutObjectRequest.class);
        final String messageBucket = RandomPrimitiveFactory.createString();
        final File file = new File(RandomPrimitiveFactory.createString());
        Mockito.when(s3.putObject(argument.capture())).thenReturn(null);

        subject.call(messageBucket, file);

        final PutObjectRequest request = argument.getValue();
        Assertions.assertEquals(messageBucket, request.getBucketName());
        Assertions.assertEquals(file.getName(), request.getKey());
        Assertions.assertEquals(file, request.getFile());
    }

    @Test
    void successfulCallOnRetry() {
        final ArgumentCaptor<PutObjectRequest> argument = ArgumentCaptor.forClass(PutObjectRequest.class);
        final String messageBucket = RandomPrimitiveFactory.createString();
        final File file = new File(RandomPrimitiveFactory.createString());
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Service);
        Mockito.when(s3.putObject(argument.capture()))
                .thenThrow(serviceException)
                .thenReturn(new PutObjectResult());

        subject.call(messageBucket, file);

        Mockito.verify(s3, Mockito.times(2)).putObject(Mockito.any(PutObjectRequest.class));
        final PutObjectRequest request = argument.getValue();
        Assertions.assertEquals(messageBucket, request.getBucketName());
        Assertions.assertEquals(file.getName(), request.getKey());
        Assertions.assertEquals(file, request.getFile());
    }

    @Test
    void badConfigurationForService() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final File file = new File(RandomPrimitiveFactory.createString());
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Client);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, file));
    }

    @Test
    void badConfigurationForUnknown() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final File file = new File(RandomPrimitiveFactory.createString());
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Unknown);
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, file));
    }

    @Test
    void badConfigurationForClient() {
        final String messageBucket = RandomPrimitiveFactory.createString();
        final File file = new File(RandomPrimitiveFactory.createString());
        Mockito.when(s3.putObject(Mockito.any(PutObjectRequest.class))).thenThrow(SdkClientException.class);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(messageBucket, file));
    }
}