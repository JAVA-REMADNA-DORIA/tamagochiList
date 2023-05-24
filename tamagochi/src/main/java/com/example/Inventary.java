package com.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Inventary extends Application {

    private ListView<String> inventaryList;
    public static ObservableList<String> inventaryItems = FXCollections.observableArrayList();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventaire");

        // Mise en page
        Label titleLabel = new Label("Inventaire");
        inventaryList = new ListView<>();
        inventaryList.setItems(inventaryItems);
        inventaryList.setPrefHeight(200);

        VBox inventaryBox = new VBox(10, titleLabel, inventaryList);
        inventaryBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(inventaryBox);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour ajouter un article à l'inventaire
    public static void addItemToInventary(String item) {
        inventaryItems.add(item);
        System.out.println(inventaryItems);
    }

    // Méthode pour afficher les articles de l'inventaire
    public static void displayInventary() {
        Stage inventaryStage = new Stage();
        inventaryStage.setTitle("Inventaire");

        ListView<String> inventaryListView = new ListView<>();
        inventaryListView.setItems(inventaryItems);
        VBox inventaryBox = new VBox(10, inventaryListView);
        inventaryBox.setPadding(new Insets(10));

        Scene inventaryScene = new Scene(inventaryBox, 400, 300);
        inventaryStage.setScene(inventaryScene);
        inventaryStage.show();
    }
}
