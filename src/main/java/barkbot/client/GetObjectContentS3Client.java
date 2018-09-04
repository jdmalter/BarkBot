package barkbot.client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

@Slf4j
public class GetObjectContentS3Client {
    static final Charset CHARSET = Charset.defaultCharset();

    @NonNull
    private final AmazonS3 s3;
    private final long retryTimeout;

    public GetObjectContentS3Client(@NonNull final AmazonS3 s3, final long retryTimeout) {
        Preconditions.checkArgument(
                retryTimeout >= 0,
                "retryTimeout (%s) must be at least 0",
                retryTimeout);

        this.s3 = s3;
        this.retryTimeout = retryTimeout;
    }

    public String call(@NonNull final String bucketName, @NonNull final String key) {
        log.info("bucketName={} key={}", bucketName, key);
        try {
            return safeCall(s3.getObject(bucketName, key));

        } catch (final AmazonServiceException e) {
            switch (e.getErrorType()) {
                case Client:
                    throw new RuntimeException("bug in configuration");

                case Service:
                    try {
                        Thread.sleep(retryTimeout);
                    } catch (final InterruptedException ignored) {
                    }
                    return safeCall(s3.getObject(bucketName, key));

                default:
                    throw new RuntimeException("bug of unknown origin");
            }

        } catch (final SdkClientException e) {
            throw new RuntimeException("bug in configuration");
        }
    }

    private String safeCall(final S3Object s3Object) {
        try {
            return IOUtils.toString(s3Object.getObjectContent(), CHARSET);

        } catch (final IOException e) {
            throw new UncheckedIOException("error in low-level I/O", e);
        }
    }
}
