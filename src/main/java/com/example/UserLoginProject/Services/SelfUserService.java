package com.example.UserLoginProject.Services;

import com.example.UserLoginProject.CustomException.PasswordMismatchException;
import com.example.UserLoginProject.CustomException.UserAlreadyExistException;
import com.example.UserLoginProject.CustomException.UserNotFoundException;
import com.example.UserLoginProject.Models.Token;
import com.example.UserLoginProject.Models.User;
import com.example.UserLoginProject.Repositories.TokenRepository;
import com.example.UserLoginProject.Repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class SelfUserService implements UserServiceInterface {

    private UserRepository userRepository;
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JWTService jwtService;

    //Dependency Injection
    public SelfUserService(UserRepository userRepository, TokenRepository tokenRepository
            , BCryptPasswordEncoder bCryptPasswordEncoder, JWTService jwtService){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public void signUp(User user) {
        Optional<User> optionalUser = userRepository.findByUserName(user.getUserName());
        if(optionalUser.isPresent())
            throw new UserAlreadyExistException("User exist", user.getUserName());

        User unsavedUser = new User();
        unsavedUser.setFirstName(user.getFirstName());
        unsavedUser.setLastName(user.getLastName());
        unsavedUser.setUserName(user.getUserName());
        unsavedUser.setActive(true);
        unsavedUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        userRepository.save(unsavedUser);
    }

    @Override
    public Token login(String userName, String password){
        Optional<User> optionalUser = userRepository.findByUserName(userName);
        if(optionalUser.isEmpty())
            throw new UserNotFoundException("User not found", userName);

        //Getting password if userName is correct
        String encodedPassword = optionalUser.get().getPassword();
        Token generatedToken = new Token();

        //Checking for password
        if(bCryptPasswordEncoder.matches(password, encodedPassword)){
            //User is valid so need to generate token
            String value = jwtService.generateToken(userName);
            Date expiryAt = jwtService.extractExpiration(value);

            generatedToken.setTokenValue(value);
            generatedToken.setUser(optionalUser.get());
            generatedToken.setExpiryAt(expiryAt);
            tokenRepository.save(generatedToken);
        }
        else
            throw new PasswordMismatchException("Incorrect Password");

        return generatedToken;
    }

    @Override
    public void logout(Token token){
        String tokenValue = token.getTokenValue();
        User user = token.getUser();

        boolean isValid = jwtService.validateToken(tokenValue, user.getUserName());

        if(isValid){
            token.setActive(false);
            tokenRepository.save(token);
        }
    }
}
