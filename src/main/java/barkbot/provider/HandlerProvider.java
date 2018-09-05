package barkbot.provider;

import barkbot.Handler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({TransformerProvider.class,
        ActionProvider.class,
        RuleProvider.class})
@RequiredArgsConstructor
public class HandlerProvider {
    @NonNull
    @Autowired
    private final TransformerProvider transformerProvider;
    @NonNull
    @Autowired
    private final ActionProvider actionProvider;
    @NonNull
    @Autowired
    private final RuleProvider ruleProvider;

    @Bean
    public Handler handler() {
        return new Handler(transformerProvider.jsonToMessageTransformer(),
                ruleProvider.imageContainsDogRule(),
                actionProvider.complainAboutMessageAction(),
                actionProvider.uploadMessageAction());
    }
}
