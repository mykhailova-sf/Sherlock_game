package com.sherlocheck.controller;

import com.sherlocheck.game.Game;
import com.sherlocheck.game.Round;
import com.sherlocheck.game.player.Player;
import com.sherlocheck.net.message.detective.Answer;
import com.sherlocheck.net.message.common.GameOver;
import com.sherlocheck.net.message.detective.Timeout;
import com.sherlocheck.net.message.storyteller.Question;
import com.sherlocheck.net.message.common.ChatMessage;
import com.sherlocheck.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicInteger;

public class GameSceneController {

    @FXML
    private Label roleLabel;

    @FXML
    private TextField chatEditor;

    @FXML
    private TextField affirmationEditor;

    @FXML
    private Button sendChatMessageButton;

    @FXML
    private Button affirmationAsTrueButton;

    @FXML
    private Button affirmationAsFalseButton;

    @FXML private VBox dynamicContainer;

    @FXML private HBox detectiveArea;
    @FXML private HBox storytellerArea;

    @FXML
    private Label affirmationLabel;

    @FXML
    private Button trueButton;

    @FXML
    private Button falseButton;

    @FXML
    private Label timerLabel;

    @FXML
    private Label round;

    @FXML
    private  Label count;

    @FXML
    private Button overGameButton;

    private Timeline timeline;

    private final ListView<HBox> chatList = new ListView<>();

    @FXML
    public void initialize() {
        setupChatHandler();
        showLabelView();
        initNewRoundInterface();
    }

    @FXML
    public void showLabelView() {
        chatList.getItems().clear();

        updateRoundLabel();

        overGameButton.setOnAction(e -> startNewGame());

        roleLabel.setText("You are " + getCurrentRoleAsText());

        sendChatMessageButton.setOnAction(e -> {
            System.out.println("Send: " + chatEditor.getText());
            Game.getGameConnection().sendMessage(new ChatMessage(chatEditor.getText()));
            addChatMessage("Me: " + chatEditor.getText(), chatList, Pos.CENTER_LEFT);
            chatEditor.clear();
        });

        Game.getGameConnection().setTimeoutHandler(()->
            Platform.runLater(() -> {
                    initNewRoundInterface();
                    setAffirmationText("");
                    setAffirmationEditorEnableState(true);
            })
        );

        Game.getGameConnection().setQuestionHandler(
                message -> Platform.runLater(
                        () -> {
                            setAffirmationText("Is this true: " + message);
                            enableDetectiveAnswerButtons();
                            updateRoundLabel();
                            startCountdownTimer(Round.TIMER_LIMIT);
                        }
                )
        );
        trueButton.setOnAction(e->detectiveSaysAnswer(true));
        falseButton.setOnAction(e->detectiveSaysAnswer(false));

        affirmationAsTrueButton.setOnAction(e -> sayAffirmationHandler(true));
        affirmationAsFalseButton.setOnAction(e -> sayAffirmationHandler(false));

        Game.getGameConnection().setAnswerHandler(
                answer -> {
                    if (Game.isAnswerCorrect(answer.isAffirmationTrue())) {
                        Game.incrementDetectiveCount();
                    } else {
                        Game.incrementStorytellerCount();
                    }
                    setAffirmationEditorEnableState(true);
                    updateCountLabel();
                }
        );
        dynamicContainer.getChildren().addAll(chatList);
    }

    private void updateRoundLabel() {
        round.setText("Round: " + Game.getCurrentRoundNumber());
    }

    private static void startNewGame() {
        Game.getGameConnection().sendMessage(new GameOver());
        SceneManager.switchScene(SceneManager.START_SCENE);
    }

    private void initNewRoundInterface() {
        if (isYouDetective()) {
            disableDetectiveAnswerButtons();
            storytellerArea.setVisible(false);
            storytellerArea.setManaged(false);
        } else {
            detectiveArea.setVisible(false);
            detectiveArea.setManaged(false);
        }
        updateCountLabel();
    }

    private void detectiveSaysAnswer(boolean isTrue) {
        timeline.stop();
        timerLabel.setVisible(false);
        timerLabel.setManaged(false);
        if (Game.isAnswerCorrect(isTrue)) {
            // Detective wins
            Game.incrementDetectiveCount();
        } else {
            Game.incrementStorytellerCount();
        }

        updateCountLabel();
        disableDetectiveAnswerButtons();

        Game.getGameConnection().sendMessage(new Answer(isTrue));
    }

    private void updateCountLabel() {
        Platform.runLater(() -> count.setText(
                "Detective: " + Game.getDetectiveCount()
                        + " Storyteller: " + Game.getStorytellerCount()
            )
        );
    }

    public void startCountdownTimer(final int count) {
        timerLabel.setVisible(true);
        timerLabel.setManaged(true);

        if (timeline != null) {
            timeline.stop();
        }

        timerLabel.setText("Time: " + count);

        AtomicInteger seconds = new AtomicInteger(count);
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> {

                    seconds.getAndDecrement();
                    timerLabel.setText("Time: " + seconds);
                    if (seconds.get() <= 0) {
                        timerLabel.setText("Time's up!");
                        timeline.stop();
                        onDetectiveTimerFinished();

                    }
                })
        );

        timeline.setCycleCount(count);
        timeline.play();
    }

    private void onDetectiveTimerFinished() {
        disableDetectiveAnswerButtons();
        Game.incrementStorytellerCount();
        updateCountLabel();

        Game.getGameConnection().sendMessage(new Timeout());
    }

    private void disableDetectiveAnswerButtons() {
        setAnswerButtonDisableState(true);
    }
    private void enableDetectiveAnswerButtons() {
        setAnswerButtonDisableState(false);
    }

    private void setAnswerButtonDisableState(boolean state) {
        trueButton.setDisable(state);
        falseButton.setDisable(state);
    }

    private void sayAffirmationHandler(boolean isTrue) {
        Game.setNewRound(new Round(isTrue));
        updateRoundLabel();

        Game.getGameConnection().sendMessage(
                new Question(affirmationEditor.getText(), isTrue)
        );
        setAffirmationEditorEnableState(false);

        setAffirmationText(
                "Your story is: " + affirmationEditor.getText()
        );

        affirmationEditor.clear();
    }

    private void setAffirmationEditorEnableState(boolean state) {
        affirmationEditor.setVisible(state);
        affirmationEditor.setManaged(state);
        affirmationAsTrueButton.setVisible(state);
        affirmationAsTrueButton.setManaged(state);
        affirmationAsFalseButton.setVisible(state);
        affirmationAsFalseButton.setManaged(state);
    }

    private void setAffirmationText(String text) {
        affirmationLabel.setVisible(true);
        affirmationLabel.setManaged(true);
        affirmationLabel.setText(text);
    }

    private static String getCurrentRoleAsText() {
        return (Game.getCurrentRole() == Player.ROLE.DETECTIVE)
                ? "Detective"
                : "Storyteller";
    }

    private void addChatMessage(String chatMessage, ListView<HBox> chatList, Pos alignment) {
        Platform.runLater(() -> {
            HBox message = new HBox();
            message.setAlignment(alignment);
            message.getChildren().add(new Label(chatMessage));
            chatList.getItems().add(message);
        });
    }

    private void setupChatHandler() {
        Game.getGameConnection().setChatMessageHandler(
                chatMessage -> addChatMessage(
                        chatMessage.getMessage(),
                        chatList,
                        Pos.CENTER_RIGHT
                )
        );
    }

    private boolean isYouDetective() {
        return Game.getCurrentRole() == Player.ROLE.DETECTIVE;
    }
}
