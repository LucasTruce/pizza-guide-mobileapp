package com.app.pizza.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class UserMessage {
    String username;
    String email;
    boolean enabled;
}
