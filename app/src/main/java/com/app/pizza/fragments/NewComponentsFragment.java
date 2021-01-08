package com.app.pizza.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pizza.R;
import com.app.pizza.adapters.ComponentAdapter;
import com.app.pizza.adapters.IngredientsAdapter;
import com.app.pizza.model.chat.Chat;
import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.reviews.Comment;
import com.app.pizza.service.ChatService;
import com.app.pizza.service.IngredientService;
import com.app.pizza.service.ServiceGenerator;
import com.app.pizza.utils.FullLengthListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewComponentsFragment extends Fragment {

    IngredientService ingredientService;
    SharedPreferences sharedPref;
    FullLengthListView ingredientsList;

    public NewComponentsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_components, container, false);

        sharedPref = view.getContext().getSharedPreferences("pref", 0);
        ingredientService = ServiceGenerator.createService(IngredientService.class, sharedPref.getString("token", ""));
        ingredientsList = view.findViewById(R.id.ingredientsList);

        Call<List<Ingredient>> call = ingredientService.getIngredients();
        call.enqueue(new Callback<List<Ingredient>>() {

            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if(response.isSuccessful()) {
                    List<Ingredient> ingredients = response.body();
                    IngredientsAdapter ingredientAdapter = new IngredientsAdapter(getActivity().getApplicationContext(), R.layout.row_ingredients, ingredients);
                    ingredientsList.setAdapter(ingredientAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {

            }
        });

        return view;
    }
}