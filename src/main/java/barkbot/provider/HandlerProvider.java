package barkbot.provider;

import barkbot.BarkBot;
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
}
