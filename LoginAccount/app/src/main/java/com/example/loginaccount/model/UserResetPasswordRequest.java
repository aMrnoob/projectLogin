package com.example.loginaccount.model;

public class UserResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;

    public UserResetPasswordRequest() {
    }

    public UserResetPasswordRequest(String email) {
        this.email = email;
    }

    public UserResetPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }

    public UserResetPasswordRequest(String email, String newPasswrod, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
