package com.cmov.tomislaaaav.acme_electronics_shop.Structures;

import java.io.Serializable;

/**
 * Created by m_bot on 10/11/2017.
 */

public class Product implements Serializable{

    private int id;
    private String maker;
    private String model;
    private int price;
    private String description;
    private int quantity;

    public Product() {

    }

    public Product(int id, String maker, String model, int price, String description, int quantity) {
        this.id = id;
        this.maker = maker;
        this.model = model;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
