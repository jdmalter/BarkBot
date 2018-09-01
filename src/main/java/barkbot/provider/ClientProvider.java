package barkbot.provider;

import barkbot.client.LabelRekognitionClient;
import barkbot.client.PostToGroupMeClient;
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
    private int MAX_LABELS;
    @Value("${MIN_CONFIDENCE}")
    private float MIN_CONFIDENCE;
    @Value("${RETRY_TIMEOUT}")
    private long RETRY_TIMEOUT;
    @Value("${BOT_ID}")
    private String BOT_ID;

    @Bean
    public LabelRekognitionClient labelRekognition() {
        final AmazonRekognition rekognition = AmazonRekognitionClientBuilder.defaultClient();
        return new LabelRekognitionClient(rekognition, MAX_LABELS, MIN_CONFIDENCE, RETRY_TIMEOUT);
    }

    @Bean
    public PostToGroupMeClient postToGroupMeClient() {
        final HttpClient client = HttpClients.createDefault();
        return new PostToGroupMeClient(client, BOT_ID);
    }
}
