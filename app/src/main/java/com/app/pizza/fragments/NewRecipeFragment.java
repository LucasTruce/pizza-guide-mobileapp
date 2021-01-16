package com.app.pizza.fragments;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.pizza.R;
import com.app.pizza.adapters.PaginationAdapter;
import com.app.pizza.model.recipe.Recipe;
import com.app.pizza.model.recipe.RecipeAdd;
import com.app.pizza.model.recipe.RecipeName;
import com.app.pizza.model.recipe.RecipeResponse;
import com.app.pizza.service.RecipeService;
import com.app.pizza.service.ServiceGenerator;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewRecipeFragment extends Fragment {

    Button add_new_recipe_button;
    SharedPreferences sharedPref;
    RecipeService recipeService;
    RecipeAdd recipeAdd;
    TextInputEditText add_recipe_name;
    TextInputEditText add_recipe_desc;

    public NewRecipeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_recipe, container, false);
        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        add_new_recipe_button = view.findViewById(R.id.add_new_recipe_button);
        add_recipe_desc = view.findViewById(R.id.add_recipe_desc);
        add_recipe_name = view.findViewById(R.id.add_recipe_name);
        recipeService = ServiceGenerator.createService(RecipeService.class, sharedPref.getString("token",""));

        add_new_recipe_button.setOnClickListener(view1 -> {
            recipeAdd = new RecipeAdd(add_recipe_name.getText().toString(),
                                        add_recipe_desc.getText().toString());
            Call<RecipeName> call = recipeService.addRecipe(recipeAdd);

            call.enqueue(new Callback<RecipeName>() {
                @Override
                public void onResponse(@NotNull Call<RecipeName> call, @NotNull Response<RecipeName> response) {

                    RecipeName recipeName = response.body();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("newRecipeId", recipeName.getId());
                    editor.apply();
                    loadFragment(new NewMediaFragment());
                }

                @Override
                public void onFailure(@NotNull Call<RecipeName> call, @NotNull Throwable t) {

                }
            });



        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }
}