package com.example;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Shop extends Application {

    private ListView<String> itemList; // liste des succès
    private ListView<String> stockList; // liste des quantités achetables
    private Label totalLabel;
    private TextField quantityTextField; // quantité que le joueur peut choisir
    private Inventary inventary;
    private Wallet wallet;
    private ArrayList<String> itemInventaryList;

    private ObservableList<Item> items; // liste des succès disponibles
    private ObservableList<Item> stock; // liste des succès disponibles en stock
    private double totalPrice = 0.0; // initialisation du prix du panier

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Magasin");

        // création des succès sous forme d'items
        items = FXCollections.observableArrayList();
        items.add(new Item("'Mon but dans la vie, c'est de respirer.'", 0.0, 1));
        items.add(new Item("Sois fainéant Tu vivras content", 5.0, 1));
        items.add(new Item("Le travail c'est la santé, ne rien faire c'est la conserver", 10.0, 1));
        items.add(new Item("Pro de la grasse mat'", 15.0, 1));
        items.add(new Item("Work smart not hard", 20.0, 1));
        items.add(new Item("'Je le ferais demain.'", 25.0, 1));
        items.add(new Item("'Si j'aurais sû, je l'aurais fait...'", 30.0, 1));
        items.add(new Item("Finalement je vais m'y mettre !", 35.0, 1));
        items.add(new Item("Il n'a que dans le dictionnaire que réussite vient avant travail", 40.0, 1));
        items.add(new Item("Le monde appartient à ceux qui se lèvent tôt", 45.0, 1));
        items.add(new Item("'J'ai terminé ce que j'avais à faire, ça te dit un mario kart ?'", 50.0, 1));
        items.add(new Item("Un vrai bosseur", 55.0, 1));
        items.add(new Item("'Arrête de travailler !'", 100.0, 1));

        // fonctionnalités du shop
        stock = FXCollections.observableArrayList();
        Label titleLabel = new Label("Bienvenue dans le magasin !");
        itemList = new ListView<>();
        itemList.setItems(getItemNames());
        itemList.setPrefHeight(200);
        itemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateStock();
        });

        // mise en page
        stockList = new ListView<>();
        stockList.setPrefHeight(200);

        inventary = new Inventary();
        itemInventaryList = new ArrayList<String>();

        quantityTextField = new TextField();

        Button addButton = new Button("Ajouter au panier");
        addButton.setOnAction(e -> toPay());

        totalLabel = new Label("Total : " + totalPrice + " ћ");

        Button checkoutButton = new Button("Paiement");
        checkoutButton.setOnAction(e -> checkout());

        HBox cartControls = new HBox(10, new Label("Quantité :"), quantityTextField, addButton);
        VBox cartBox = new VBox(10, new Label("Panier"), itemList, cartControls, totalLabel, checkoutButton);
        cartBox.setPadding(new Insets(10));

        VBox stockBox = new VBox(10, new Label("Informations"), stockList);
        stockBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(titleLabel);
        root.setCenter(new HBox(cartBox, stockBox));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // calcul du prix
    private void toPay() {
        try {
            int quantity = Integer.parseInt(quantityTextField.getText());
            String selectedItem = itemList.getSelectionModel().getSelectedItem();
            Item item = getItemByName(selectedItem);

            if (item != null && item.getStock() >= quantity) {
                double subtotal = quantity * item.getPrice();
                totalPrice += subtotal;
                totalLabel.setText("Total : " + totalPrice + " ћ");
                item.setStock(item.getStock() - quantity);
                quantityTextField.clear();
                updateStock();
                itemInventaryList.add(item.getName());
            } else {
                System.out.println("Quantité invalide ou article déjà acheté");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur de conversion de la quantité");
        }
    }

    // Effectue le paiement
private void checkout() {
    System.out.println("Paiement effectué. Total : " + totalPrice + " ћ");
    wallet.removeFromBank(totalPrice); 
    int i = 0;
    while (i < itemInventaryList.size()) {
        Inventary.addItemToInventary(itemInventaryList.get(i));
        i++;
    }
    Inventary.displayInventary();
    System.out.println(itemInventaryList);
}

    // affichage des items
    private ObservableList<String> getItemNames() {
        ObservableList<String> itemNames = FXCollections.observableArrayList();
        for (Item item : items) {
            itemNames.add(item.getName());
        }
        return itemNames;
    }

    // mise à jour des stocks
    private void updateStock() {
        stockList.getItems().clear();
        String selectedItem = itemList.getSelectionModel().getSelectedItem();
        Item item = getItemByName(selectedItem);
        if (item != null) {
            stockList.getItems().add("Nom : " + item.getName());
            stockList.getItems().add("Prix : " + item.getPrice() + " ћ");
            stockList.getItems().add("Stock : " + item.getStock());
        }
    }

    private Item getItemByName(String name) {
        for (Item item : items) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public Shop(Wallet wallet) {
        this.wallet = wallet;
    }

    // accesseurs et mutateurs
    private class Item {
        private String name;
        private double price;
        private int stock;

        public Item(String name, double price, int stock) {
            this.name = name;
            this.price = price;
            this.stock = stock;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
}
