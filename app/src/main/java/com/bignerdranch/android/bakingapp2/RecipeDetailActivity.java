package com.bignerdranch.android.bakingapp2;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.bignerdranch.android.bakingapp2.Model.BakingLab;
import com.bignerdranch.android.bakingapp2.Model.Recipe;
import com.bignerdranch.android.bakingapp2.Model.Steps;
import com.bignerdranch.android.bakingapp2.Services.BakingService;

import java.util.ArrayList;

/*
* Once the user selects a recipe he is redirected to the Details of that Recipe
* That includes displaying the recipes Ingredients and recipe Steps.
* */
public class RecipeDetailActivity extends SingleFragmentActivity implements RecipeDetailFragment.Callbacks
, RecipeStepDetailFragment.Callbacks{

    public static final String EXTRA_RECIPE_ID = "com.bignerdranch.android.bakingapp2.recipe_id";

    //Encapsulation of the variables that this class uses
    public static Intent newIntent(Context context, int recipe_id){
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID,recipe_id);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(EXTRA_RECIPE_ID)){
            int recipe_id = getIntent().getIntExtra(EXTRA_RECIPE_ID, 1);
            BakingService.startActionUpdateStoredIngredients(this, recipe_id);
            BakingService.startActionRefreshWidget(this);

            getSupportActionBar()
                    .setTitle(BakingLab.getInstance(this).getRecipe(recipe_id).getName());
        }
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_masterdetail;
    }

    //To provide the necessary fragment at runtime with no need to have tight coupling
    //for a certain fragment in layout
    @Override
    protected Fragment createFragment() {
        int recipe_id = getIntent().getIntExtra(EXTRA_RECIPE_ID,1);
        Recipe recipe = BakingLab.getInstance(this).getRecipe(recipe_id);
        Fragment fragment =  RecipeDetailFragment
                .newInstance(recipe.getIngredients(), recipe.getSteps());

        if (findViewById(R.id.detail_fragment_container) != null){
            onStepSelected(0);
        }

        return fragment;
    }

    //Proper navigation, so that when an app is ran through a widget and the user decides to go back
    //The user goes to the home screen app
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Implementing the Callbacks interfaces to handle the tablet layout view so that when
    //a user presses an item, a fragment gets replaced.
    @Override
    public void onStepSelected(int adapterPosition) {
        int recipe_id = getIntent().getIntExtra(EXTRA_RECIPE_ID,1);
        Recipe recipe = BakingLab.getInstance(this).getRecipe(recipe_id);
        ArrayList<Steps> stepsList = recipe.getSteps();

        if (findViewById(R.id.detail_fragment_container) == null){
            Intent intent = RecipeStepDetailActivity.newIntent
                    (this,
                            stepsList,
                            adapterPosition);
            startActivity(intent);
        } else {
            Fragment recipeDetailedSteps = RecipeStepDetailFragment
                    .newInstance(stepsList.get(adapterPosition).getLongDescription(),
                                    stepsList.get(adapterPosition).getVideoURL(),
                                    adapterPosition, stepsList.size());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, recipeDetailedSteps)
                    .commit();

        }
    }

    @Override
    public void onStepForward(int nextPosition) {
        swapStepDetailFragment(nextPosition);
    }

    @Override
    public void onStepBackward(int previousPosition) {
        swapStepDetailFragment(previousPosition);
    }

    private void swapStepDetailFragment(int position){
        int recipe_id = getIntent().getIntExtra(EXTRA_RECIPE_ID,1);
        Recipe recipe = BakingLab.getInstance(this).getRecipe(recipe_id);
        ArrayList<Steps> stepsList = recipe.getSteps();
        Steps steps = stepsList.get(position);

        Fragment recipeDetailedSteps = RecipeStepDetailFragment
                .newInstance(steps.getLongDescription(),steps.getVideoURL(),position
                ,stepsList.size());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.detail_fragment_container, recipeDetailedSteps)
                .commit();
    }

}
