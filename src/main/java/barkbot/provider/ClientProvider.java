package barkbot.provider;

import barkbot.client.DecryptKMSClient;
import barkbot.client.DetectLabelsRekognitionClient;
import barkbot.client.PostToGroupMeClient;
import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.AWSKMSClientBuilder;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
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
    @Value("${SECRET}")
    private String secret;

    @Bean
    public DecryptKMSClient decryptKMSClient() {
        final AWSKMS awskms = AWSKMSClientBuilder.defaultClient();
        return new DecryptKMSClient(awskms);
    }

    @Bean
    public DetectLabelsRekognitionClient detectLabelsRekognitionClient() {
        final AmazonRekognition rekognition = AmazonRekognitionClientBuilder.defaultClient();
        return new DetectLabelsRekognitionClient(rekognition, maxLabels, minConfidence, retryTimeout);
    }

    @Bean
    public PostToGroupMeClient postToGroupMeClient() {
        final HttpClient client = HttpClients.createDefault();
        final String botId = decryptKMSClient().decrypt(secret);
        return new PostToGroupMeClient(client, botId);
    }
}
