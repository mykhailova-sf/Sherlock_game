package com.sherlocheck.game;

public class Round {

    public static final int TIMER_LIMIT = 40;
    private Boolean isAffirmationTrue = null;

    public Round(boolean affirmationIsTrue) {
        setAffirmationStatus(affirmationIsTrue);
    }

    private void setAffirmationStatus(Boolean affirmationTrue) {
        isAffirmationTrue = affirmationTrue;
    }

    public Boolean getAffirmationTrue() {
        if (isAffirmationTrue == null) {
            throw new IllegalStateException("isAffirmationTrue has not been initialized yet.");
        }

        return isAffirmationTrue;
    }
}
