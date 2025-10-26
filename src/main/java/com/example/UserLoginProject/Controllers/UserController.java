package com.example.UserLoginProject.Controllers;

import com.example.UserLoginProject.DTOs.*;
import com.example.UserLoginProject.Models.Token;
import com.example.UserLoginProject.Models.User;
import com.example.UserLoginProject.Services.UserServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserServiceInterface userServiceInterface;

    //Dependency Injection Constructor based
    public UserController(UserServiceInterface userServiceInterface){
        this.userServiceInterface = userServiceInterface;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO){
        User user = SignUpRequestDTO.convert(signUpRequestDTO);
        userServiceInterface.signUp(user);

        return ResponseEntity.ok("SIGNIN SUCCESSFULL");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Token generatedToken = userServiceInterface.login(loginRequestDTO.getUserName(), loginRequestDTO.getPassword());
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setGeneratedToken(generatedToken.getTokenValue());

        return ResponseEntity.ok(loginResponseDTO);
    }
    public ResponseEntity<String> logout(@RequestBody Token token){
        userServiceInterface.logout(token);
        return ResponseEntity.ok("LOGOUT SUCCESS");
    }
}
