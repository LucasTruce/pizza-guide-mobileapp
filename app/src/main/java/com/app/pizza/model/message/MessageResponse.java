package com.app.pizza.model.message;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private int number;
    private List<Message> content;
    private int totalPages;
    @SerializedName("size")
    private int pageSize;
    private boolean last;
}
