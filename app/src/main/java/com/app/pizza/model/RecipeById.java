package com.app.pizza.model;

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
