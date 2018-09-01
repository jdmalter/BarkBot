package barkbot.action;

import barkbot.model.Message;

public interface Action {
    void execute(final Message message);
}
