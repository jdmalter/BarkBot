package barkbot.provider;

import barkbot.transformer.JsonToMessageTransformer;
import barkbot.transformer.UrlToByteBufferTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransformerProvider {
    @Bean
    public JsonToMessageTransformer jsonToMessageTransformer() {
        return new JsonToMessageTransformer(new ObjectMapper());
    }

    @Bean
    public UrlToByteBufferTransformer stringToUrlTransformer() {
        return new UrlToByteBufferTransformer();
    }
}
