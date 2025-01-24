package com.example.demo.model;

public class UserResetPasswordRequest {
    
    private String email;
    private String newPassword;
	private String otp;

    public UserResetPasswordRequest() {
    }

    public UserResetPasswordRequest(String email) {
        this.email = email;
    }
    
    public UserResetPasswordRequest(String email, String newPassword) {
        this.email = email;
        this.newPassword = newPassword;
    }
    
    public UserResetPasswordRequest(String email, String newPassword, String otp) {
        this.email = email;
        this.newPassword = newPassword;
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
