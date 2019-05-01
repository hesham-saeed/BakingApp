package com.bignerdranch.android.bakingapp2;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.bakingapp2.Model.Ingredient;
import com.bignerdranch.android.bakingapp2.Model.Steps;
import com.bignerdranch.android.bakingapp2.Utils.BakingUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    private static final String ARG_INGREDIENTS = "ingredients";
    private static final String ARG_STEPS = "steps";
    private Callbacks mCallbacks;

    @BindView(R.id.tv_ingredients)
    TextView mIngredients;
    @BindView(R.id.rv_recipe_steps)
    RecyclerView mRecipeSteps;

    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Steps> mStepsList;

    public static Fragment newInstance(ArrayList<Ingredient> ingredients, ArrayList<Steps> steps){
        Bundle args = new Bundle();
        args.putSerializable(ARG_INGREDIENTS, ingredients);
        args.putSerializable(ARG_STEPS, steps);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface Callbacks{
        void onStepSelected(int adapterPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIngredientList = (ArrayList<Ingredient>) getArguments().getSerializable(ARG_INGREDIENTS);
        mStepsList = (ArrayList<Steps>) getArguments().getSerializable(ARG_STEPS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ButterKnife.bind(this, v);

        mIngredients.setText(BakingUtils.setupIngredients(mIngredientList));

        mRecipeSteps.setAdapter(new StepsAdapter(mStepsList));

        mRecipeSteps.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    private class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder>{

        private ArrayList<Steps> mStepsList;
        public StepsAdapter(ArrayList<Steps> steps){
            mStepsList = steps;
        }

        @NonNull
        @Override
        public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            return new StepHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull StepHolder holder, int position) {
            holder.bindStep(mStepsList.get(position).getShortDescription());
        }

        @Override
        public int getItemCount() {
            if (mStepsList != null)
            return mStepsList.size();
            return 0;
        }

        class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView stepTextView;
            public StepHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);

                stepTextView = (TextView) itemView;
            }
            public void bindStep(String step){
                stepTextView.setText(step);
            }

            @Override
            public void onClick(View v) {
                Steps steps = mStepsList.get(getAdapterPosition());
                mCallbacks.onStepSelected(getAdapterPosition());
            }
        }
    }


}
