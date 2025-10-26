package com.example.UserLoginProject.ControllerAdvice;

import com.example.UserLoginProject.CustomException.PasswordMismatchException;
import com.example.UserLoginProject.CustomException.UserAlreadyExistException;
import com.example.UserLoginProject.CustomException.UserNotFoundException;
import com.example.UserLoginProject.Models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionalHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException message){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("An account with the username" + message.getUserName() +
                        " already exists. Please use a different email or log in with this one.");
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException message){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(message.getUserName() + "not found. Please enter correct username");
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException message){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Please enter correct password");
    }
}
