package com.app.pizza.model.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RecipeName {

    private int id;
    private String name;
    private String description;
}
