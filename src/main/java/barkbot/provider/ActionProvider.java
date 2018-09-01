package barkbot.provider;

import barkbot.action.ComplainAboutMessageAction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({ClientProvider.class})
@Configuration
@RequiredArgsConstructor
public class ActionProvider {
    @NonNull
    private final ClientProvider clientProvider;

    @Bean
    public ComplainAboutMessageAction complainAboutMessageAction() {
        return new ComplainAboutMessageAction(clientProvider.postToGroupMeClient());
    }
}
