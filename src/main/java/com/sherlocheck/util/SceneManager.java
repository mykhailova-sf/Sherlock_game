package com.sherlocheck.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


import java.io.IOException;

public class SceneManager {

    public static String START_SCENE = "/StartScene.fxml";
    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath) {
        System.out.println("Switching Scene to " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
