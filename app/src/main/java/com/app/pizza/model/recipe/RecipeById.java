package com.app.pizza.model.recipe;

import com.app.pizza.model.reviews.Comment;
import com.app.pizza.model.component.Component;
import com.app.pizza.model.media.Media;
import com.app.pizza.model.step.Step;
import com.app.pizza.model.user.User;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecipeById {
    private String name;
    private String description;
    private User user;
    private List<Step> steps;
    private List<Component> components;
    private List<Comment> reviews;
    private List<Media> mediaList;
}
