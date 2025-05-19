package com.sherlocheck.util;

import com.sherlocheck.game.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupWindow {

    public static void show(String message, String scoreText, Runnable onConfirm) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Game Over");

        popupStage.setOnCloseRequest(event -> {
            onConfirm.run();
        });

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            onConfirm.run();
            popupStage.close();
        });

        VBox layout = new VBox(15, getLabel(message), getLabel(scoreText), getLabel(Game.whoIsWinner()), okButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        popupStage.setScene(new Scene(layout, 600, 200));
        popupStage.showAndWait();
    }

    private static Label getLabel(String message) {
        Label label = new Label(message);
        label.setFont(Font.font("System", FontWeight.BOLD, 14));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        return label;
    }
}
