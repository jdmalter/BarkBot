package barkbot.client;

import barkbot.factory.RandomPrimitiveFactory;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

@ExtendWith(MockitoExtension.class)
class GetObjectContentS3ClientTest {
    private GetObjectContentS3Client subject;
    @Mock
    private AmazonS3 s3;
    private long retryTimeout;

    @BeforeEach
    void setUp() {
        // no sleeping during testing
        retryTimeout = 0;
        subject = new GetObjectContentS3Client(s3, retryTimeout);
    }

    @Test
    void successfulCall() {
        final String expected = RandomPrimitiveFactory.createString();
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final S3Object s3Object = new S3Object();
        s3Object.setObjectContent(IOUtils.toInputStream(expected, GetObjectContentS3Client.CHARSET));
        Mockito.when(s3.getObject(bucketName, key)).thenReturn(s3Object);

        final String actual = subject.call(bucketName, key);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void successfulCallOnRetry() {
        final String expected = RandomPrimitiveFactory.createString();
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final S3Object s3Object = new S3Object();
        s3Object.setObjectContent(IOUtils.toInputStream(expected, GetObjectContentS3Client.CHARSET));
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Service);
        Mockito.when(s3.getObject(bucketName, key))
                .thenThrow(serviceException)
                .thenReturn(s3Object);

        final String actual = subject.call(bucketName, key);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void badConfigurationForService() {
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Client);
        Mockito.when(s3.getObject(bucketName, key)).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(bucketName, key));
    }

    @Test
    void badConfigurationForUnknown() {
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final AmazonServiceException serviceException = new AmazonServiceException(RandomPrimitiveFactory.createString());
        serviceException.setErrorType(AmazonServiceException.ErrorType.Unknown);
        Mockito.when(s3.getObject(bucketName, key)).thenThrow(serviceException);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(bucketName, key));
    }

    @Test
    void badConfigurationForClient() {
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        Mockito.when(s3.getObject(bucketName, key)).thenThrow(SdkClientException.class);

        Assertions.assertThrows(RuntimeException.class, () -> subject.call(bucketName, key));
    }

    @Test
    void badConnection() throws IOException {
        final String bucketName = RandomPrimitiveFactory.createString();
        final String key = RandomPrimitiveFactory.createString();
        final InputStream inputStream = Mockito.mock(InputStream.class);
        final S3Object s3Object = new S3Object();
        s3Object.setObjectContent(inputStream);
        Mockito.when(s3.getObject(bucketName, key)).thenReturn(s3Object);
        Mockito.when(inputStream.read(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(IOException.class);

        Assertions.assertThrows(UncheckedIOException.class, () -> subject.call(bucketName, key));
    }
}