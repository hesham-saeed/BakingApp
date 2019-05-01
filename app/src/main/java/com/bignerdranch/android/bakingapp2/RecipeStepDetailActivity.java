package com.bignerdranch.android.bakingapp2;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.android.bakingapp2.Model.Recipe;
import com.bignerdranch.android.bakingapp2.Model.Steps;
import com.bignerdranch.android.bakingapp2.Utils.BakingUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepDetailActivity extends SingleFragmentActivity
        implements RecipeStepDetailFragment.Callbacks{


    private static final String EXTRA_STEP = "com.bignerdranch.android.bakingapp2.step";
    private static final String EXTRA_POSITION = "com.bignerdranch.android.bakingapp2.position";

    public static Intent newIntent(Context context, ArrayList<Steps> steps, int position){
        Intent intent = new Intent(context, RecipeStepDetailActivity.class);
        intent.putExtra(EXTRA_STEP, steps);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        ArrayList<Steps> stepsList = (ArrayList<Steps>) getIntent().getSerializableExtra(EXTRA_STEP);
        int adapterPos = getIntent().getIntExtra(EXTRA_POSITION,0);
        return RecipeStepDetailFragment.newInstance
                        (stepsList.get(adapterPos).getLongDescription(),
                            stepsList.get(adapterPos).getVideoURL(),
                        getIntent().getIntExtra(EXTRA_POSITION,0),
                                stepsList.size());
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
        ArrayList<Steps> stepsList = (ArrayList<Steps>) getIntent().getSerializableExtra(EXTRA_STEP);
        Steps steps = stepsList.get(position);

        Fragment recipeDetailedSteps = RecipeStepDetailFragment
                .newInstance(steps.getLongDescription(),steps.getVideoURL(),position,stepsList.size());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, recipeDetailedSteps)
                .commit();
    }
}
