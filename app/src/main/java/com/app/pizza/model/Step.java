package com.app.pizza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Step {
    String name;
    String description;
    String time;
}
