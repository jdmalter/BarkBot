package barkbot.rule;

import barkbot.model.Message;

public interface Rule {
    boolean accepts(Message message);
}
