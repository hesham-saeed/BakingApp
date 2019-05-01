package com.bignerdranch.android.bakingapp2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.android.bakingapp2.Model.BakingLab;
import com.bignerdranch.android.bakingapp2.Model.Recipe;
import com.bignerdranch.android.bakingapp2.Services.BakingService;
import com.bignerdranch.android.bakingapp2.Utils.BakingUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/*
* This class handles displays the list of recipes using a recyclerview which is an inner class
* */
public class SelectRecipeActivity extends AppCompatActivity {

    private static final String TAG = SelectRecipeActivity.class.getName();
    private RecipesAdapter adapter;
    private FetchData mFetchData;

    @BindView(R.id.rv_select_piece)
    RecyclerView mRecipesRecylerView;

    class FetchData extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            String response = null;
            if (BakingLab.getInstance(SelectRecipeActivity.this).getRecipes() == null
                    || BakingLab.getInstance(SelectRecipeActivity.this).getRecipes().size() == 0 )
            {

                try {
                    URL url = new URL(BakingUtils.DATA_URL);
                    response = BakingUtils.getResponse(url);
                    Log.d(TAG, "Downloading data!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                ArrayList<Recipe> recipes = BakingLab.getInstance(SelectRecipeActivity.this).getRecipes();
                adapter.swapData(recipes);
                BakingService.startActionRefreshWidget(SelectRecipeActivity.this);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s != null){
                Log.d(TAG, "Parsing Response");
                ArrayList<Recipe> recipes = BakingUtils.getRecipes(SelectRecipeActivity.this, s);
                BakingLab.getInstance(SelectRecipeActivity.this).setRecipes(recipes);
                adapter.swapData(recipes);
                BakingService.startActionRefreshWidget(SelectRecipeActivity.this);
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFetchData.cancel(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_piece);

        ButterKnife.bind(this);

        adapter = new RecipesAdapter();
        mFetchData = new FetchData();
        mFetchData.execute();
        //mRecipeList = BakingLab.getInstance(this).getRecipes();

        mRecipesRecylerView.setAdapter(adapter);

        //If it's a tablet: change the recyclerview layout into a grid layout
        //Otherwise remain as a linear layout
        if (getResources().getConfiguration().smallestScreenWidthDp >= 600)
            mRecipesRecylerView.setLayoutManager(new GridLayoutManager(this,3));
        else
            mRecipesRecylerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle(R.string.select_recipe_default_title);

    }

    private class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder>{
        private ArrayList<Recipe> mRecipeList = new ArrayList<>();
        public RecipesAdapter(){
        }
        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_item,parent,false);
            return new RecipeHolder(v);
        }

        public void swapData(ArrayList<Recipe> recipeList){
            mRecipeList = recipeList;
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
            String recipeName = mRecipeList.get(position).getName();
            holder.bindRecipe(recipeName);
        }

        @Override
        public int getItemCount() {
            if (mRecipeList != null)
            return mRecipeList.size();
            return 0;
        }

        class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            //Each item will contain a simple textview
            private TextView recipeText;
            public RecipeHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                recipeText = (TextView)itemView;

            }

            //An interface to change the private variable text to preserve encapsulation
            public void bindRecipe(String recipeName){
                recipeText.setText(recipeName);
            }

            @Override
            public void onClick(View v) {
                Recipe recipe = mRecipeList.get(getAdapterPosition());

                Intent RecipeDetailsIntent = RecipeDetailActivity.newIntent(
                        SelectRecipeActivity.this, recipe.getId());
                startActivity(RecipeDetailsIntent);
            }
        }
    }


}
