package com.bignerdranch.android.bakingapp2.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {

    int mId;
    String mName;
    ArrayList<Ingredient> mIngredients;
    ArrayList<Steps> mSteps;

    public int getId() {
        return mId;
    }

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients, ArrayList<Steps> steps) {
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public ArrayList<Steps> getSteps() {
        return mSteps;
    }
}
