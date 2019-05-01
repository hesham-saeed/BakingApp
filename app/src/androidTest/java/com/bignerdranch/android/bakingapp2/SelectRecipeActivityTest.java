package com.bignerdranch.android.bakingapp2;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import android.support.test.espresso.*;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;


@RunWith(AndroidJUnit4.class)
public class SelectRecipeActivityTest {
    private static final String EXTRA_RECIPE_ID = "com.bignerdranch.android.bakingapp2.recipe_id";

    @Rule
    public ActivityTestRule<SelectRecipeActivity> intentsTestRule =
            new ActivityTestRule<>(SelectRecipeActivity.class);

    @Test
    public void selectRecyclerViewItem_checksRecipeDetailActivityRecyclerViewIsDisplayed(){
        onView(withId(R.id.rv_select_piece))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.rv_recipe_steps))
                .check(matches(isDisplayed()));
    }

    @Test
    public void selectRecyclerViewItem_checksRecipeDetailActivityIngredientsTextViewIsDisplayed(){
        onView(withId(R.id.rv_select_piece))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.tv_ingredients))
                .check(matches(isDisplayed()));
    }

}
