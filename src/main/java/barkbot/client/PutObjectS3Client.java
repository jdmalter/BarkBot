package barkbot.client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.common.base.Preconditions;
import lombok.NonNull;

import java.io.InputStream;

public class PutObjectS3Client {
    @NonNull
    private final AmazonS3 s3;
    private final long retryTimeout;

    public PutObjectS3Client(@NonNull final AmazonS3 s3, final long retryTimeout) {
        Preconditions.checkArgument(
                retryTimeout >= 0,
                "retryTimeout (%s) must be at least 0",
                retryTimeout);

        this.s3 = s3;
        this.retryTimeout = retryTimeout;
    }

    public void call(@NonNull final String messageBucket, @NonNull final String key, @NonNull final InputStream input) {
        try {
            s3.putObject(new PutObjectRequest(messageBucket, key, input, new ObjectMetadata()));

        } catch (final AmazonServiceException e) {
            switch (e.getErrorType()) {
                case Client:
                    throw new RuntimeException("bug in configuration");

                case Service:
                    try {
                        Thread.sleep(retryTimeout);
                    } catch (final InterruptedException ignored) {
                    }
                    s3.putObject(new PutObjectRequest(messageBucket, key, input, new ObjectMetadata()));
                    return;

                default:
                    throw new RuntimeException("bug of unknown origin");
            }

        } catch (final SdkClientException e) {
            throw new RuntimeException("bug in configuration");
        }
    }
}
