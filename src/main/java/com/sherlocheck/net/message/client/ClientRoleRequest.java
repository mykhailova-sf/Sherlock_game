package com.sherlocheck.net.message.client;

import com.sherlocheck.game.player.Player;

public class ClientRoleRequest implements ClientMessage {

    public Player.ROLE requestClientRole() {
        return Player.ROLE.STORYTELLER;
    }
}
