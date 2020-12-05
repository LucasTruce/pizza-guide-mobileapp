package com.app.pizza.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Comment {
    int score;
    User user;
    String description;
}
