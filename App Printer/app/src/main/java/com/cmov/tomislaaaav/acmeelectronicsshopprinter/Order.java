package com.cmov.tomislaaaav.acmeelectronicsshopprinter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by m_bot on 10/11/2017.
 */

public class Order implements Serializable{
    private String id;
    private Date date;
    private int totalPrice = 0;
    private ArrayList<Product> products = new ArrayList<Product>();

    public Order() {

    }

    public Order(String id, Date date, ArrayList<Product> products) {
        this.id = id;
        this.date = date;
        this.products = products;
        for (int i = 0; i < products.size(); i++) {
            totalPrice += products.get(i).getPrice() * products.get(i).getQuantity();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
