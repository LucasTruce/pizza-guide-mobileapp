package com.app.pizza.service;

import com.app.pizza.model.component.Component;
import com.app.pizza.model.step.Step;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StepSerivce {

    @POST("/recipes/{id}/steps")
    Call<List<Step>> addSteps(@Body List<Step> steps,
                                        @Path(value = "id") int id);
}

