package com.example.ecommerceplatform.services;


import com.example.ecommerceplatform.models.UserEntity;
import com.example.ecommerceplatform.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserEntity> allUsers(){
        return userRepository.findAll();
    }

}
