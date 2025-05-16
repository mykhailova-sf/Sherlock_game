package com.sherlocheck.net.message.detective;

import com.sherlocheck.net.message.TextMessage;

public class Answer extends TextMessage {
    private final boolean isAffirmationTrue;

    public Answer(boolean isTrue) {
        super(isTrue ? "True" : "False");

        this.isAffirmationTrue = isTrue;
    }

    public boolean isAffirmationTrue() {
        return isAffirmationTrue;
    }
}
