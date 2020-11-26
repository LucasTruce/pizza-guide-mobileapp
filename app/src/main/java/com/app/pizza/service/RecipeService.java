package com.app.pizza.service;

import com.app.pizza.model.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("/recipes")
    Call<RecipeResponse> getRecipes();

    @GET("recipes")
    Call<RecipeResponse> getRecipes(@Query("pageNumber") int pageNumber);
}
