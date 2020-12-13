package com.app.pizza.service;

import com.app.pizza.model.chat.ChatPagination;
import com.app.pizza.model.message.Message;
import com.app.pizza.model.message.MessageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageService {

    @GET("/chats/{id}/messages")
    Call<MessageResponse> getMessages(@Path(value="id") int id);
}
