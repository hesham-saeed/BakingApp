package com.bignerdranch.android.bakingapp2.Model;

import android.content.Context;

import com.bignerdranch.android.bakingapp2.Utils.BakingUtils;

import java.util.ArrayList;

public class BakingLab {
    private static ArrayList<Recipe> mRecipes;

    private static BakingLab sBakingLab;

    private BakingLab(){

    }

    public static BakingLab getInstance(Context context) {
        if (sBakingLab == null){
            sBakingLab = new BakingLab();
        }
        return sBakingLab;
    }

    public void setRecipes(ArrayList<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(int id){
        if (mRecipes == null) return null;
        for (int i = 0;i<mRecipes.size();i++){
            if (mRecipes.get(i).getId() == id){
                return mRecipes.get(i);
            }
        }
        return null;
    }
}
