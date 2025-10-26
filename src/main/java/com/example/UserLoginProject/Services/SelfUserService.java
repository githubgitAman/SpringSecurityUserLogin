package com.example.UserLoginProject.Services;

import com.example.UserLoginProject.CustomException.PasswordMismatchException;
import com.example.UserLoginProject.CustomException.UserAlreadyExistException;
import com.example.UserLoginProject.CustomException.UserNotFoundException;
import com.example.UserLoginProject.Models.Token;
import com.example.UserLoginProject.Models.User;
import com.example.UserLoginProject.Repositories.TokenRepository;
import com.example.UserLoginProject.Repositories.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private AuthenticationManager authenticationManager;
    private final ApplicationContext applicationContext;

    //Dependency Injection
    public SelfUserService(UserRepository userRepository, TokenRepository tokenRepository
            , BCryptPasswordEncoder bCryptPasswordEncoder, JWTService jwtService,
                           AuthenticationManager authenticationManager, ApplicationContext applicationContext){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.applicationContext = applicationContext;
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
//        unsavedUser.setRole(user.getRole());

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
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            if(authentication.isAuthenticated()) {
                //User is valid so need to generate token
                String value = jwtService.generateToken(userName);
                Date expiryAt = jwtService.extractExpiration(value);
                generatedToken.setTokenValue(value);
                generatedToken.setUser(optionalUser.get());
                generatedToken.setExpiryAt(expiryAt);
                generatedToken.setActive(true);

                tokenRepository.save(generatedToken);
            }
            else
                throw new PasswordMismatchException("Incorrect Password");

        return generatedToken;
    }

    @Override
    public void logout(String token) {

        String userName = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByUserName(userName);

        UserDetails userDetails = applicationContext
                .getBean(CustomUserDetailsService.class)
                .loadUserByUsername(userName);


        boolean isVerified = jwtService.validateToken(token, userDetails);
        if(isVerified){
            Token changedToken = tokenRepository.findByTokenValue(token);
            changedToken.setExpiryAt(new Date(System.currentTimeMillis()));
            changedToken.setActive(false);
            user.get().setActive(false);
            userRepository.save(user.get());
            tokenRepository.save(changedToken);
        }
    }
}
