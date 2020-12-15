package com.app.pizza.model.chat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatPagination {

    private int number;
    private List<Chat> content;
    private int totalPages;
    @SerializedName("size")
    private int pageSize;
    private boolean last;
}
