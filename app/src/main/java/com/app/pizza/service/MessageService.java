package com.app.pizza.service;

import com.app.pizza.model.message.Message;
import com.app.pizza.model.message.MessageResponse;
import com.app.pizza.model.message.MessageSend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageService {

    @GET("/chats/{id}/messages")
    Call<MessageResponse> getMessages(@Path(value="id") int id);

    @GET("/chats/{id}/messages")
    Call<MessageResponse> getMessages(@Path(value="id") int id, @Query("pageNumber") int pageNumber);

    @POST("/chats/{id}/messages")
    Call<Message> saveMessage(@Path(value = "id") int id, @Body MessageSend messageSend);

}
