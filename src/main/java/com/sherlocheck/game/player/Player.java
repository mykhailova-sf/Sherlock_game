package com.sherlocheck.game.player;

public interface Player {
    enum ROLE {
        STORYTELLER, DETECTIVE
    }

    ROLE getRole();
}
