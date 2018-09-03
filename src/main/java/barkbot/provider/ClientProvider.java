package barkbot.provider;

import barkbot.client.DetectLabelsRekognitionClient;
import barkbot.client.GetObjectContentS3Client;
import barkbot.client.PostToGroupMeClient;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class ClientProvider {
    @Value("${MAX_LABELS}")
    private int maxLabels;
    @Value("${MIN_CONFIDENCE}")
    private float minConfidence;
    @Value("${RETRY_TIMEOUT}")
    private long retryTimeout;
    @Value("${BUCKET_NAME}")
    private String bucketName;
    @Value("${KEY}")
    private String key;

    @Bean
    public DetectLabelsRekognitionClient detectLabelsRekognitionClient() {
        final AmazonRekognition rekognition = AmazonRekognitionClientBuilder.defaultClient();
        return new DetectLabelsRekognitionClient(rekognition, maxLabels, minConfidence, retryTimeout);
    }

    @Bean
    public GetObjectContentS3Client getObjectContentS3Client() {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        return new GetObjectContentS3Client(s3, retryTimeout);
    }

    @Bean
    public PostToGroupMeClient postToGroupMeClient() {
        final HttpClient client = HttpClients.createDefault();
        final GetObjectContentS3Client getObjectContentS3Client = getObjectContentS3Client();
        final Supplier<String> botIdSupplier = () -> getObjectContentS3Client.call(bucketName, key);
        return new PostToGroupMeClient(client, botIdSupplier);
    }
}
