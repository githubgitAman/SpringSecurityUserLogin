package com.example.UserLoginProject.DTOs;

import com.example.UserLoginProject.Models.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDTO {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;

    //Convert DTO to Model
    public static User convert(SignUpRequestDTO signUpRequestDTO){
        User user = new User();
        user.setFirstName(signUpRequestDTO.getFirstName());
        user.setLastName(signUpRequestDTO.getLastName());
        user.setUserName(signUpRequestDTO.getUserName());
        user.setPassword(signUpRequestDTO.getPassword());
        user.setActive(true);

        return user;
    }
}
