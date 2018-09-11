package barkbot;

import barkbot.action.ComplainAboutMessageAction;
import barkbot.action.UploadMessageAction;
import barkbot.model.Message;
import barkbot.rule.ImageContainsDogRule;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BarkBot {
    @NonNull
    private final ImageContainsDogRule imageContainsDogRule;
    @NonNull
    private final ComplainAboutMessageAction complainAboutMessageAction;
    @NonNull
    private final UploadMessageAction uploadMessageAction;

    public void accept(@NonNull final Message message) {
        if (!imageContainsDogRule.accepts(message)) {
            complainAboutMessageAction.execute(message);
            uploadMessageAction.execute(message);
        }
    }
}
