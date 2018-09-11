package barkbot.provider;

import barkbot.rule.ImageContainsDogRule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ClientProvider.class})
@Configuration
@RequiredArgsConstructor
public class RuleProvider {
    @NonNull
    private final ClientProvider clientProvider;

    @Bean
    public ImageContainsDogRule imageContainsDogRule() {
        return new ImageContainsDogRule(clientProvider.downloadClient(),
                clientProvider.detectLabelsRekognitionClient());
    }
}
