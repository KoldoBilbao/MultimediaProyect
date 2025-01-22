package com.example.roomtest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "portfolio")
public class Portfolio {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String coinId;
    private String name;
    private String symbol;
    private double quantity;
    private double price;

    // Constructor, getters y setters

    public Portfolio(String coinId, String name, String symbol, double quantity, double price) {
        this.coinId = coinId;
        this.name = name;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
