package com.sherlocheck.net.message.server;

import com.sherlocheck.game.player.Player;

public class ClientRoleResponse implements ServerMessage {

    private final Player.ROLE role;

    public ClientRoleResponse(Player.ROLE role) {
        this.role = role;
    }

    public Player.ROLE getClientRole() {
        return role;
    }
}
