package com.sherlocheck.game;

import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.GameConnection;

public class Game {

    private static Round currentRound;

    private static Player.ROLE playerRole;
    private static GameConnection gameConnection;
    private static int numberRound = 0;
    private static int detectiveCount = 0;
    private static int storytellerCount = 0;


    public static int getCurrentRoundNumber() {
        return numberRound;
    }

    public static void resetRoundNumber() {
        numberRound = 0;
    }

    public static void setNewRound(Round round) {
        currentRound = round;
        if (currentRound == null) {
            System.out.println("null round");
            resetRoundNumber();
        } else {
            System.out.println("num: " + numberRound);
            numberRound++;
        }
    }

    public static String whoIsWinner() {
        Player.ROLE winnerRole;

        if (detectiveCount == storytellerCount) {
            return "The game ended in a draw.";
        } else if(detectiveCount > storytellerCount) {
            winnerRole = Player.ROLE.DETECTIVE;
        } else {
            winnerRole = Player.ROLE.STORYTELLER;
        }

        return  (winnerRole == getCurrentRole())
                ? "You won"
                : "You lost";
    }

    public static Round getCurrentRound() {
        return currentRound;
    }

    public static void setGameConnection(GameConnection newGameConnection) {
        gameConnection = newGameConnection;
    }

    public static GameConnection getGameConnection() {
        return gameConnection;
    }

    public static boolean isAnswerCorrect(boolean isTrue) {
        return Game.getCurrentRound().getAffirmationTrue() == isTrue;
    }

    public static void incrementDetectiveCount() {
        detectiveCount++;
    }

    public static void incrementStorytellerCount() {
        storytellerCount++;
    }

    public static int getStorytellerCount() {
        return storytellerCount;
    }

    public static int getDetectiveCount() {
        return detectiveCount;
    }

    public static void resetCounters() {
        detectiveCount = 0;
        storytellerCount = 0;
    }

    public static void resetGame() {
        resetCounters();
        setNewRound(null);
        try {
            getGameConnection().close();
        } catch (Exception e) {
            System.out.println("Error closing GameConnection: " + e.getMessage());
        }
    }

    public static void setPlayerRole(Player.ROLE playerRole) {
        Game.playerRole = playerRole;
    }

    public static Player.ROLE getCurrentRole() {
        return playerRole;
    }

    public static String getScoreText() {
        return "Detective: " + Game.getDetectiveCount() + " Storyteller: " + Game.getStorytellerCount();
    }
}
