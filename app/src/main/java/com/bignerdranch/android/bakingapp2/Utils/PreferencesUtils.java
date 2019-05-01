package com.bignerdranch.android.bakingapp2.Utils;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bignerdranch.android.bakingapp2.IngredientsWidget;
import com.bignerdranch.android.bakingapp2.R;
import com.bignerdranch.android.bakingapp2.Services.BakingService;
import com.bignerdranch.android.bakingapp2.Model.Recipe;

public class PreferencesUtils {
    public static final String KEY_RECIPE_ID = "recipe_id";

    synchronized public static void updateKeyRecipeId(Context context, Recipe recipe) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit()
                .putInt(KEY_RECIPE_ID, recipe.getId())
                .apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, IngredientsWidget.class));

        IngredientsWidget.updateAllAppWidget(context, appWidgetManager,appWidgetIds);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view_widget);
    }

    public static int getLatestKeyRecipeId(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(KEY_RECIPE_ID,-1);
    }


}
