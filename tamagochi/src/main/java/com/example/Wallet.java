package com.example;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Wallet {
    //fournit des méthodes pour accéder et modifier la valeur de cette propriété en temps réel
    private DoubleProperty bank; 
    
    //initialisation du porte-monnaie à 0
    public Wallet() {
        bank = new SimpleDoubleProperty(0.0);
    }

    public DoubleProperty bankProperty() {
        return bank;
    }

    //getter
    public double getBank() { 
        return bank.get();
    }

    public void registerInventary(Inventary inventary) {
    }

    //incrementation du porte-monnaie selon les gains
    public void addToBank(double recompense) { 
        bank.set(bank.get() + recompense);
    }

    public void removeFromBank(double amount) {
        bank.set(bank.get() - amount);
    }
}