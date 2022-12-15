package com.tw.ecommerceplatform.services;


import com.tw.ecommerceplatform.models.UserEntity;
import com.tw.ecommerceplatform.repositories.UserRepository;
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
