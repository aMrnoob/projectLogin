package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.model.UserLoginRequest;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;

    public boolean register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() || 
            userRepository.findByEmail(user.getEmail()).isPresent()) {
            return false; 
        }
        userRepository.save(user);
        return true;
    }

    public Optional<User> login(UserLoginRequest userLoginRequest) {
    	String username = userLoginRequest.getUsername();
    	String password = userLoginRequest.getPassword();
    	
        Optional<User> user = userRepository.findByUsername(username);
        return user.filter(u -> u.getPassword().equals(password));
    }
}
