package com.app.pizza.service;
import com.app.pizza.model.media.Media;
import com.app.pizza.model.recipe.RecipeResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MediaService {

    @Multipart
    @POST("recipes/{id}/media")
    Call<List<Media>> addImage(@Part List<MultipartBody.Part> image,
                        @Path(value = "id") int id);
}
