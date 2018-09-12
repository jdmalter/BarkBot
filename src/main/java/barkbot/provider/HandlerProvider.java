package barkbot.provider;

import barkbot.BarkBot;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({ActionProvider.class,
        RuleProvider.class})
@RequiredArgsConstructor
public class HandlerProvider {
    @NonNull
    private final ActionProvider actionProvider;
    @NonNull
    private final RuleProvider ruleProvider;

    @Bean
    public BarkBot handler() {
        return new BarkBot(ruleProvider.imageContainsDogRule(),
                actionProvider.complainAboutMessageAction(),
                actionProvider.uploadMessageAction());
    }

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
