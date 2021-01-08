package com.app.pizza.model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChatError {
    private int status;
    private String message;
}
