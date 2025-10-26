package com.example.UserLoginProject.Repositories;

import com.example.UserLoginProject.Models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);
}
