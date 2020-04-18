package com.example.warehouseinventoryapp;

import androidx.annotation.NonNull;

public class Item {

    //global vars
    private double cost;
    private int quantity;
    private String name;
    private String desc = "";
    private boolean frozen =  false; //false by default

    //constructor
    public Item(String name, int quantity, double cost, String desc, boolean frozen){
        setCost(cost);
        setName(name);
        setQuantity(quantity);
        setDesc(desc);
        setFrozen(frozen);
    }

    //getter and setters

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " Quantity:" + getQuantity() + " Cost:" + getCost();
    }
}
