package com.bignerdranch.android.bakingapp2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bignerdranch.android.bakingapp2.Model.BakingLab;
import com.bignerdranch.android.bakingapp2.Model.Ingredient;
import com.bignerdranch.android.bakingapp2.Utils.PreferencesUtils;

import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService{
    private String TAG = this.getClass().getSimpleName();
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int recipe_id = PreferencesUtils.getLatestKeyRecipeId(this);
        if (recipe_id == -1 || BakingLab.getInstance(this).getRecipe(recipe_id) == null){
            Log.d(TAG,"recipe_id == -1 || BakingLab.getInstance(this).getRecipe(recipe_id) == null" );
            return new AppWidgetListView(this.getApplicationContext(), null);
        }
        ArrayList<Ingredient> list = BakingLab.getInstance(this).getRecipe(recipe_id).getIngredients();
        return new AppWidgetListView(this.getApplicationContext(), list);
    }
}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory{

    private ArrayList<Ingredient> dataList;
    private Context context;

    public AppWidgetListView( Context context, ArrayList<Ingredient> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        int recipe_id = PreferencesUtils.getLatestKeyRecipeId(context);
        if (!(recipe_id == -1 || BakingLab.getInstance(context).getRecipe(recipe_id) == null)){
            dataList = BakingLab.getInstance(context).getRecipe(recipe_id).getIngredients();
        }
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        if (dataList == null) return 0;
        return dataList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        int recipe_id = PreferencesUtils.getLatestKeyRecipeId(context);

        String ingredient = dataList.get(position).getQuantity() + " " + dataList.get(position).getMeasure()
                + " of " + dataList.get(position).getIngredient();

        views.setTextViewText(R.id.tv_widget_list_item, ingredient);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe_id);
        views.setOnClickFillInIntent(R.id.tv_widget_list_item, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
