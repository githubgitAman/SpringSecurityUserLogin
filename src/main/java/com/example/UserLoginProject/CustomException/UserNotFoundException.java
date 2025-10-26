package com.example.UserLoginProject.CustomException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotFoundException extends RuntimeException{
    private String userName;
    public UserNotFoundException(String message, String userName){
        super(message);
        this.userName = userName;
    }
}
