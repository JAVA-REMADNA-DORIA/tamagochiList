package com.example;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class Task extends Application {

    private ObservableList<TaskItem> tasks = FXCollections.observableArrayList();
    private Wallet wallet;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tamago List");

        // Création d'un tableau
        TableView<TaskItem> tableView = new TableView<>();
        tableView.setItems(tasks);

        TableColumn<TaskItem, String> taskColumn = new TableColumn<>("Tâche");
        taskColumn.setCellValueFactory(data -> data.getValue().taskProperty());

        TableColumn<TaskItem, Double> priceColumn = new TableColumn<>("Prix");
        priceColumn.setCellValueFactory(data -> data.getValue().priceProperty().asObject());

        tableView.getColumns().addAll(taskColumn, priceColumn);

        // insertion de l'image
        ImageView imageView = new ImageView("https://i.redd.it/upqb6reo69n61.jpg");

        // Redimensionner l'image
        double desiredWidth = 200;
        double desiredHeight = 200; 
        imageView.setFitWidth(desiredWidth);
        imageView.setFitHeight(desiredHeight);
        imageView.setPreserveRatio(true); // ne déforme pas l'image

        // Création des champs de texte
        TextField inputField = new TextField();
        inputField.setPromptText("Entrez votre tâche");

        TextField priceField = new TextField();
        priceField.setPromptText("Entrez le prix");

        // Partie porte-monnaie
        wallet = new Wallet();
        Label bankLabel = new Label();
        bankLabel.textProperty().bindBidirectional(wallet.bankProperty(), new NumberStringConverter("#0.00")); //met à jour automatiquement le texte (solde affiché sous forme de deux décimales)

        // Création des boutons
        Button addButton = new Button("Ajouter une tâche");
        addButton.setOnAction(e -> {
            String task = inputField.getText().trim();
            double price = Double.parseDouble(priceField.getText().trim());
            if (!task.isEmpty() && price >= 0) {
                TaskItem item = new TaskItem(task, price);
                tasks.add(item);
                inputField.clear();
                priceField.clear();
            }
        });

        Button removeButton = new Button("Supprimer");
        removeButton.setOnAction(e -> {
            TaskItem selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                tasks.remove(selectedItem);
            }
        });

        Button modifyButton = new Button("Modifier");
        modifyButton.setOnAction(e -> {
            TaskItem selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String newTask = inputField.getText().trim();
                double newPrice = Double.parseDouble(priceField.getText().trim());
                if (!newTask.isEmpty() && newPrice >= 0) {
                    selectedItem.setTask(newTask);
                    selectedItem.setPrice(newPrice);
                    tableView.refresh();
                    inputField.clear();
                    priceField.clear();
                }
            }
        });

        Button checkButton = new Button("Check");
        checkButton.setOnAction(e -> {
            TaskItem selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                double price = selectedItem.getPrice();
                wallet.addToBank(price);
                tasks.remove(selectedItem);
                System.out.println("Tâche validée ! Récompense : " + price + " ћ");
            }
        });

        Button shopButton = new Button("Boutique");
        shopButton.setOnAction(e -> {
            Shop shop = new Shop(wallet);
            Stage shopStage = new Stage();
            shop.start(shopStage);
        });

        Button inventaryButton = new Button("Inventaire");
        inventaryButton.setOnAction(e -> {
            Inventary.displayInventary();
        });

        // Mise en page
        HBox gameBox = new HBox(10, shopButton, inventaryButton);
        gameBox.setPadding(new Insets(20));

        HBox buttonBox = new HBox(10, addButton, removeButton, modifyButton, checkButton);
        buttonBox.setPadding(new Insets(10));

        VBox inputBox = new VBox(10, inputField, priceField);
        inputBox.setPadding(new Insets(10));

        HBox bankBox = new HBox(10, new Label("Porte-monnaie :"), bankLabel);
        bankBox.setPadding(new Insets(10));

        VBox imageBox = new VBox();
        imageBox.getChildren().add(imageView);

        VBox rightBox = new VBox(10, gameBox, imageBox);

        BorderPane root = new BorderPane();
        root.setCenter(tableView);
        root.setBottom(buttonBox);
        root.setLeft(inputBox);
        root.setTop(bankBox);
        root.setRight(rightBox);

Scene scene = new Scene(root, 800, 800);
primaryStage.setScene(scene);
primaryStage.show();
    }

    // Classe qui contient les accesseurs et mutateurs de chaque tâche avec son prix
    public static class TaskItem {
        private String task;
        private Double price;

        public TaskItem(String task, Double price) {
            this.task = task;
            this.price = price;
        }

        public String getTask() {
            return task;
        }

        public Double getPrice() {
            return price;
        }

        public StringProperty taskProperty() {
            return new SimpleStringProperty(task);
        }

        public DoubleProperty priceProperty() {
            return new SimpleDoubleProperty(price);
        }

        public void setTask(String newTask) {
            task = newTask;
        }

        public void setPrice(Double newPrice) {
            price = newPrice;
        }
    }
}