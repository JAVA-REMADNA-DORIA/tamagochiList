package com.example;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HangmanGame extends Application {

    private String secretWord = "hangman";
    private StringBuilder guessedWord;
    private int attemptsLeft = 6;

    private Label wordLabel;
    private TextField letterField;
    private Button guessButton;
    private Label attemptsLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hangman Game");

        wordLabel = new Label();
        letterField = new TextField();
        guessButton = new Button("Guess");
        guessButton.setOnAction(e -> guessLetter());
        attemptsLabel = new Label("Attempts left: " + attemptsLeft);

        HBox inputBox = new HBox(10, letterField, guessButton);
        VBox root = new VBox(10, wordLabel, inputBox, attemptsLabel);
        root.setPadding(new Insets(10));

        updateWordLabel();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void guessLetter() {
        String letter = letterField.getText().toLowerCase();
        letterField.clear();

        if (letter.length() != 1) {
            showAlert("Please enter a single letter.");
            return;
        }

        if (!Character.isLetter(letter.charAt(0))) {
            showAlert("Please enter a valid letter.");
            return;
        }

        char guessedLetter = letter.charAt(0);
        boolean found = false;
        for (int i = 0; i < secretWord.length(); i++) {
            if (Character.toLowerCase(secretWord.charAt(i)) == guessedLetter) {
                guessedWord.setCharAt(i, secretWord.charAt(i));
                found = true;
            }
        }

        if (!found) {
            attemptsLeft--;
        }

        updateWordLabel();
        updateAttemptsLabel();

        if (attemptsLeft == 0) {
            endGame(false);
        } else if (guessedWord.toString().equals(secretWord)) {
            endGame(true);
        }
    }

    private void updateWordLabel() {
        if (guessedWord == null) {
            guessedWord = new StringBuilder(secretWord.length());
            for (int i = 0; i < secretWord.length(); i++) {
                if (Character.isLetter(secretWord.charAt(i))) {
                    guessedWord.append('_');
                } else {
                    guessedWord.append(secretWord.charAt(i));
                }
            }
        }

        wordLabel.setText("Word: " + guessedWord.toString());
    }

    private void updateAttemptsLabel() {
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
    }

    private void endGame(boolean won) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over");

        if (won) {
            alert.setHeaderText("Congratulations! You won!");
        } else {
            alert.setHeaderText("Game over. You lost.");
        }

        alert.setContentText("The word was: " + secretWord);
        alert.showAndWait();

        // Restart the game
        secretWord = "donne moi vingt cellya";
        guessedWord = null;
        attemptsLeft = 6;
        updateWordLabel();
        updateAttemptsLabel();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
