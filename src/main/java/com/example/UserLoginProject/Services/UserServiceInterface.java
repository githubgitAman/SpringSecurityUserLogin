package com.example.UserLoginProject.Services;

import com.example.UserLoginProject.Models.Token;
import com.example.UserLoginProject.Models.User;

public interface UserServiceInterface {
    void signUp(User user);
    Token login(String userName, String password);
    void logout(Token token);
}
