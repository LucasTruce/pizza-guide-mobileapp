package com.app.pizza.service;

import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.recipe.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IngredientService {

    @GET("/ingredients")
    Call<List<Ingredient>> getIngredients();


}
