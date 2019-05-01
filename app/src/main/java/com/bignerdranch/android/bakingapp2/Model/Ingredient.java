package com.bignerdranch.android.bakingapp2.Model;


import java.io.Serializable;

public class Ingredient implements Serializable{

    int quantity;
    String measure;
    String ingredient;

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }
}
