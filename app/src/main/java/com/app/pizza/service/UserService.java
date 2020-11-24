package com.app.pizza.service;

import com.app.pizza.model.UserAuthResponse;
import com.app.pizza.model.UserLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("/auth/signin")
    Call<UserAuthResponse> authorization(@Body UserLogin login);
}
