package com.app.pizza.model.message;

import com.app.pizza.model.user.UserMessage;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Message {
    String message;
    String sendTime;
    UserMessage sender;
}
