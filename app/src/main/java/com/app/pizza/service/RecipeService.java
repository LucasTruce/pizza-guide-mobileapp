package com.app.pizza.service;

import com.app.pizza.model.recipe.Recipe;
import com.app.pizza.model.recipe.RecipeAdd;
import com.app.pizza.model.recipe.RecipeById;
import com.app.pizza.model.recipe.RecipeName;
import com.app.pizza.model.recipe.RecipeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("/recipes")
    Call<RecipeResponse> getRecipes();

    @GET("recipes")
    Call<RecipeResponse> getRecipes(@Query("pageNumber") int pageNumber);

    @GET("recipes/{id}")
    Call<RecipeById> getRecipeById(@Path(value="id") int id);

    @POST("recipes")
    Call<RecipeName> addRecipe(@Body RecipeAdd recipeAdd);
}
