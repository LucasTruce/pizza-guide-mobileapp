package com.app.pizza.model.component;

import com.app.pizza.model.ingredient.Ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Component {
    String amount;
    Ingredient ingredients;
}
