package com.example.roomtest;

public class Crypto {

    private String id;
    private String name;
    private String symbol;
    private int market_cap_rank;
    private String image;
    private double current_price;
    private double price_change_percentage_24h;
    private double max_supply;
    private double circulating_supply;
    private long market_cap;



    // Constructor
    public Crypto(String id, String name, String symbol, int market_cap_rank, String image, double current_price,
                  long market_cap, double max_supply, double circulating_supply,
                  double price_change_percentage_24h) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.market_cap_rank = market_cap_rank;
        this.image = image;
        this.current_price = current_price;
        this.market_cap = market_cap;
        this.max_supply = max_supply;
        this.circulating_supply = circulating_supply;
        this.price_change_percentage_24h = price_change_percentage_24h; // Inicializar campo
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getMarket_cap_rank() {
        return market_cap_rank;
    }

    public void setMarket_cap_rank(int market_cap_rank) {
        this.market_cap_rank = market_cap_rank;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(double current_price) {
        this.current_price = current_price;
    }

    public double getPrice_change_percentage_24h() {
        return price_change_percentage_24h;
    }

    public void setPrice_change_percentage_24h(double price_change_percentage_24h) {
        this.price_change_percentage_24h = price_change_percentage_24h;
    }

    public double getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(double max_supply) {
        this.max_supply = max_supply;
    }

    public double getCirculating_supply() {
        return circulating_supply;
    }

    public void setCirculating_supply(double circulating_supply) {
        this.circulating_supply = circulating_supply;
    }

    public long getMarket_cap() {
        return market_cap;
    }

    public void setMarket_cap(long market_cap) {
        this.market_cap = market_cap;
    }

    @Override
    public String toString() {
        return "Crypto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", market_cap_rank=" + market_cap_rank +
                ", image='" + image + '\'' +
                ", current_price=" + current_price +
                ", price_change_percentage_24h=" + price_change_percentage_24h +
                '}';
    }
}
