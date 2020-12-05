package com.app.pizza.service;

import com.app.pizza.model.user.UserAuthResponse;
import com.app.pizza.model.user.UserLogin;
import com.app.pizza.model.user.UserRegister;
import com.app.pizza.model.user.UserRegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("/auth/signin")
    Call<UserAuthResponse> authorization(@Body UserLogin login);

    @POST("/auth/signup")
    Call<UserRegisterResponse> registration(@Body UserRegister register);
}
