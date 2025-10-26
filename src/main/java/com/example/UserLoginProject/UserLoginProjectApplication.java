package com.example.UserLoginProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//For autoupdating createdAt and updatedAt
@EnableJpaAuditing
@SpringBootApplication
public class UserLoginProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserLoginProjectApplication.class, args);
	}

}
