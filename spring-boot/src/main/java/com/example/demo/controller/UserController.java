package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.model.UserLoginRequest;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {
	
	@Autowired
    private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<ApiResponse> register(@RequestBody User user) {
	    if (userService.register(user)) {
	        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
	    } else {
	        return ResponseEntity.badRequest().body(new ApiResponse(false, "Username or Email already exists"));
	    }
	}

	@PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        Optional<User> validUser = userService.login(userLoginRequest);
        if (validUser.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Login successful"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Invalid username or password"));
        }
    }
}
