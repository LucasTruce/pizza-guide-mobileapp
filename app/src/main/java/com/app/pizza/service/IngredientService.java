package com.app.pizza.service;

import com.app.pizza.model.component.Component;
import com.app.pizza.model.ingredient.Ingredient;
import com.app.pizza.model.recipe.RecipeResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface IngredientService {

    @GET("/ingredients")
    Call<List<Ingredient>> getIngredients();

    @POST("/recipes/{id}/components")
    Call<List<Component>> addComponents(@Body List<Component> components,
                                        @Path(value = "id") int id);


}
