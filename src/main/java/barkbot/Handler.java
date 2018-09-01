package barkbot;

import barkbot.action.ComplainAboutMessageAction;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import barkbot.transformer.JsonToMessageTransformer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Handler {
    @NonNull
    private final JsonToMessageTransformer jsonToMessageTransformer;
    @NonNull
    private final ImageContainsDogRule imageContainsDogRule;
    @NonNull
    private final ComplainAboutMessageAction complainAboutMessageAction;

    public void accept(@NonNull final String json) {
        final Message message = jsonToMessageTransformer.convert(json);

        if (!imageContainsDogRule.accepts(message)) {
            complainAboutMessageAction.execute(message);
        }
    }
}
