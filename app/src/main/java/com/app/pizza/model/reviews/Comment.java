package com.app.pizza.model.reviews;

import com.app.pizza.model.user.User;

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
