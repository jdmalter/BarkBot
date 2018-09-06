package barkbot.provider;

import barkbot.client.DetectLabelsRekognitionClient;
import barkbot.client.PostToGroupMeClient;
import barkbot.client.PutObjectS3Client;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientProvider {
    @Value("${MAX_LABELS}")
    private int maxLabels;
    @Value("${MIN_CONFIDENCE}")
    private float minConfidence;
    @Value("${RETRY_TIMEOUT}")
    private long retryTimeout;
    @Value("${BOT_ID}")
    private String botId;

    @Bean
    public DetectLabelsRekognitionClient detectLabelsRekognitionClient() {
        final AmazonRekognition rekognition = AmazonRekognitionClientBuilder.defaultClient();
        return new DetectLabelsRekognitionClient(rekognition, maxLabels, minConfidence, retryTimeout);
    }

    @Bean
    public PostToGroupMeClient postToGroupMeClient() {
        final HttpClient client = HttpClients.createDefault();
        return new PostToGroupMeClient(client, botId);
    }

    @Bean
    public PutObjectS3Client putObjectS3Client() {
        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        return new PutObjectS3Client(s3, retryTimeout);
    }
}
