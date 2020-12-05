package com.app.pizza.model.recipe;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeResponse {
    private int number;
    private List<Recipe> content;
    private int totalPages;
    @SerializedName("size")
    private int pageSize;
    private boolean last;
}
