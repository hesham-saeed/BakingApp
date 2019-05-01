package com.bignerdranch.android.bakingapp2.Utils;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.bakingapp2.Model.Ingredient;
import com.bignerdranch.android.bakingapp2.Model.Recipe;
import com.bignerdranch.android.bakingapp2.Model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BakingUtils {

    public static final String DATA_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    private static String loadJSONFromAsset(Context context) {

        final String[] Response2 = new String[1];
        try {
            URL url = new URL(DATA_URL);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                         Response2[0] = response.body().toString();
                    }
                }
            });
            return Response2[0];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //Parsings the json file
    public static ArrayList<Recipe> getRecipes(Context context, String RecipesString) {
        ArrayList<Recipe> recipeList = new ArrayList<>();
        //String RecipesString = loadJSONFromAsset(context);
        try {

            JSONArray RecipesArray = new JSONArray(RecipesString);

            for (int i = 0; i < RecipesArray.length(); i++) {

                JSONObject singleRecipe = RecipesArray.getJSONObject(i);

                int id = singleRecipe.getInt("id");
                String name = singleRecipe.getString("name");

                JSONArray ingredientsArray = singleRecipe.getJSONArray("ingredients");
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    int quantity = ingredientsArray.getJSONObject(j).getInt("quantity");
                    String measure = ingredientsArray.getJSONObject(j).getString("measure");
                    String ingredient = ingredientsArray.getJSONObject(j).getString("ingredient");
                    Ingredient singleIngredient = new Ingredient(quantity,measure, ingredient);
                    ingredientsList.add(singleIngredient);
                }

                JSONArray stepsArray = singleRecipe.getJSONArray("steps");
                ArrayList<Steps> stepsList = new ArrayList<>();
                for (int j =0;j<stepsArray.length();j++){
                    int stepId = stepsArray.getJSONObject(j).getInt("id");
                    String shortDescription = stepsArray.getJSONObject(j).getString("shortDescription");
                    String longDescription = stepsArray.getJSONObject(j).getString("description");
                    String videoURL = stepsArray.getJSONObject(j).getString("videoURL");
                    String thumbnailURL = stepsArray.getJSONObject(j).getString("thumbnailURL");
                    Steps singleRecipeSteps = new Steps(stepId, shortDescription, longDescription, videoURL,thumbnailURL);
                    stepsList.add(singleRecipeSteps);
                }

                Recipe recipe = new Recipe(id,name,ingredientsList,stepsList);
                recipeList.add(recipe);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    //concatenating the ingredients list using a StringBuilde and returning it as a string
    public static String setupIngredients(ArrayList<Ingredient> mIngredientList){
        StringBuilder ingredientsBuilder = new StringBuilder();

        String ingredients = "Ingredients:\n";
        for (int i = 0; i<mIngredientList.size(); i++){
            ingredientsBuilder.append("- ");
            ingredientsBuilder.append(mIngredientList.get(i).getQuantity());
            ingredientsBuilder.append(" ");
            ingredientsBuilder.append(mIngredientList.get(i).getMeasure() + " of ");
            ingredientsBuilder.append(mIngredientList.get(i).getIngredient());

            if (i != mIngredientList.size()-1)
                ingredientsBuilder.append("\n");

        }
        ingredients += ingredientsBuilder.toString();
        return ingredients;
    }

    public static String getResponse(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        try{
            InputStream in = con.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            con.disconnect();
        }

    }

}
