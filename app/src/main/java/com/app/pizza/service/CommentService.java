package com.app.pizza.service;

import com.app.pizza.model.chat.ChatPagination;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.reviews.AddComment;
import com.app.pizza.model.reviews.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentService {
    @POST("/recipes/{id}/reviews")
    Call<AddComment> addComment(@Body AddComment comment,
                                @Path(value = "id") int id);
}
