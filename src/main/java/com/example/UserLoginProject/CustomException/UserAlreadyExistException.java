package com.example.UserLoginProject.CustomException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyExistException extends RuntimeException{
    private String userName;
    public UserAlreadyExistException(String message, String userName){
        super(message);
        this.userName = userName;
    }
}
