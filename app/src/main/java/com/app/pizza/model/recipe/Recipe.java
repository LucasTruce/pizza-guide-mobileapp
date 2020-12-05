package com.app.pizza.model.recipe;

import com.app.pizza.model.media.Media;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Recipe {

    private int id;
    private String name;
    private String description;
    private List<Media> mediaList;
}
