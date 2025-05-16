package com.sherlocheck.controller;

import com.sherlocheck.game.Game;
import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.GameDiscovery;
import com.sherlocheck.net.Starter;
import com.sherlocheck.net.message.client.ClientRoleRequest;
import com.sherlocheck.util.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class StartSceneController {

    @FXML private VBox dynamicContainer;
    @FXML private RadioButton labelRadio;
    @FXML private RadioButton listRadio;
    @FXML private Label waitedRole;

    @FXML
    public void initialize() {
        ToggleGroup viewToggleGroup = new ToggleGroup();
        labelRadio.setToggleGroup(viewToggleGroup);
        listRadio.setToggleGroup(viewToggleGroup);

        labelRadio.setSelected(true);
        showLabelView();
        Game.resetGame();
    }

    @FXML
    public void showLabelView() {
        dynamicContainer.getChildren().clear();

        Label label = new Label("Choice the role");

        ToggleGroup subToggleGroup = new ToggleGroup();

        RadioButton detectiveOption = new RadioButton("Detective");
        RadioButton storytellerOption = new RadioButton("Storyteller");

        detectiveOption.setToggleGroup(subToggleGroup);
        storytellerOption.setToggleGroup(subToggleGroup);
        detectiveOption.setSelected(true);

        waitedRole = new Label();
        waitedRole.setVisible(false);

        VBox subBox = getvBox(detectiveOption, storytellerOption);

        dynamicContainer.getChildren().addAll(label, subBox, waitedRole);
    }

    private VBox getvBox(RadioButton detectiveOption, RadioButton storytellerOption) {
        Button button = new Button("Start new game");
        button.setOnAction(e -> {
            System.out.println(".......... TODO create and start new game");
            Starter.startServer(() ->
                    Platform.runLater(
                            () -> SceneManager.switchScene("/MainScene.fxml")
                    )
            );

            Player.ROLE playerRole = detectiveOption.isSelected()
                    ? Player.ROLE.DETECTIVE
                    : Player.ROLE.STORYTELLER;

            waitedRole.setVisible(true);

            waitedRole.setText( (playerRole == Player.ROLE.DETECTIVE ? "Detective" : "Storyteller")
                    + " is waiting"
            );
            button.setVisible(false);
            Game.setPlayerRole(
                    playerRole
            );
            Game.setNewRound(null);
        });

        return new VBox(10, detectiveOption, storytellerOption, button);
    }

    @FXML
    public void showListView() {
        dynamicContainer.getChildren().clear();

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(
                GameDiscovery.discover(Starter.SERVER_PORT)
        );

        dynamicContainer.getChildren().add(listView);

        Button button = new Button("Connect");
        button.setOnAction(e -> {
            if (listView.getSelectionModel() != null) {
                Starter.startClient(listView.getSelectionModel().getSelectedItem());
                Game.getGameConnection().sendMessage(new ClientRoleRequest());
            }
        });

        dynamicContainer.getChildren().add(button);
    }
}
