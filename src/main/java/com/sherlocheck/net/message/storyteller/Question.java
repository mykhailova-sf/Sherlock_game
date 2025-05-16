package com.sherlocheck.net.message.storyteller;

import com.sherlocheck.net.message.TextMessage;

public class Question extends TextMessage {
    private final boolean affirmationIsTrue;

    public Question(String content, boolean isTrue) {
        super(content);

        this.affirmationIsTrue = isTrue;
    }

    public boolean isAffirmationIsTrue() {
        return affirmationIsTrue;
    }
}
