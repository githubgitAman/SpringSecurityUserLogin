package com.example.UserLoginProject.DTOs;

import com.example.UserLoginProject.Models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String generatedToken;
}
