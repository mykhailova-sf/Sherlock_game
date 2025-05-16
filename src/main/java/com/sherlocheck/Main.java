package com.sherlocheck;

import com.sherlocheck.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene(SceneManager.START_SCENE);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
