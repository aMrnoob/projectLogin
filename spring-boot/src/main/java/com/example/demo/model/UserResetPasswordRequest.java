package com.example.demo.model;

public class UserResetPasswordRequest {
    
    private String email;

    public UserResetPasswordRequest() {
    }

    public UserResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
