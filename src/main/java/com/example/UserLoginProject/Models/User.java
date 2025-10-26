package com.example.UserLoginProject.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseModel{

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private boolean isActive;
    @ManyToMany
    private List<Role> role;
}
