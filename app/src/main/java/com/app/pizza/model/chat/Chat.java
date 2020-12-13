package com.app.pizza.model.chat;

import com.app.pizza.model.message.Message;
import com.app.pizza.model.user.UserMessage;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Chat {
    private String id;
    private UserMessage sender;
    private UserMessage receiver;
    private List<Message> message;

}
