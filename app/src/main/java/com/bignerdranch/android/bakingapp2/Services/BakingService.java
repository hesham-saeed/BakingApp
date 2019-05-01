package com.bignerdranch.android.bakingapp2.Services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.bignerdranch.android.bakingapp2.IngredientsWidget;
import com.bignerdranch.android.bakingapp2.Model.BakingLab;
import com.bignerdranch.android.bakingapp2.Model.Recipe;
import com.bignerdranch.android.bakingapp2.R;
import com.bignerdranch.android.bakingapp2.RecipeDetailActivity;
import com.bignerdranch.android.bakingapp2.Utils.BakingUtils;
import com.bignerdranch.android.bakingapp2.Utils.PreferencesUtils;
/*
* This class is responsible for storage of the recently viewed recepie
* for quick reference in the widget.
* */

public class BakingService extends IntentService {

    private static final String ACTION_UPDATE_STORED_INGREDIENTS = "com.bignerdranch.android.bakingapp2.update_ingredients";
    private static final String ACTION_REFRESH_WIDGET = "com.bignerdranch.android.bakingapp2.refresh_widget";

    public BakingService() {
        super("BakingService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        if (action == ACTION_UPDATE_STORED_INGREDIENTS){
            int  recipe_id =  intent.getIntExtra(RecipeDetailActivity.EXTRA_RECIPE_ID,-1);
            Recipe recipe = BakingLab.getInstance(this).getRecipe(recipe_id);
            PreferencesUtils.updateKeyRecipeId(this, recipe);

        }

        else if (action == ACTION_REFRESH_WIDGET){

            /*
            int recipe_id = PreferencesUtils.getLatestKeyRecipeId(this);
            Recipe recipe;
            String ingredients = null;
            if (recipe_id != -1) {
                recipe = BakingLab.getInstance(this).getRecipe(recipe_id);
                if (recipe != null){
                    ingredients = recipe.getName() + " ";
                    ingredients += BakingUtils.setupIngredients(recipe.getIngredients());
                }

            }
            else {
                ingredients = getString(R.string.default_ingredient);
            }*/

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));

            IngredientsWidget.updateAllAppWidget(this, appWidgetManager,appWidgetIds);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_widget);
        }
    }

    public static void startActionUpdateStoredIngredients(Context context, int recipe_id){
        Intent intent = new Intent(context, BakingService.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe_id);
        intent.setAction(ACTION_UPDATE_STORED_INGREDIENTS);
        context.startService(intent);
    }

    public static void startActionRefreshWidget(Context context){
        Intent intent = new Intent(context, BakingService.class);
        intent.setAction(ACTION_REFRESH_WIDGET);
        context.startService(intent);
    }
}
