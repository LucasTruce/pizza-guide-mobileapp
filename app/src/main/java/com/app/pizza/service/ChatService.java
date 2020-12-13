package com.app.pizza.service;

import com.app.pizza.model.chat.ChatPagination;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatService {

    @GET("/chats")
    Call<ChatPagination> getChats(@Query("pageNumber") int pageNumber);

    @GET("/chats")
    Call<ChatPagination> getChats();
}
