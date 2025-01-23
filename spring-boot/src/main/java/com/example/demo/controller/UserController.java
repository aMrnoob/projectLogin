package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.ApiResponse;
import com.example.demo.model.OtpEntry;
import com.example.demo.model.OtpStorage;
import com.example.demo.model.User;
import com.example.demo.model.UserLoginRequest;
import com.example.demo.model.UserResetPasswordRequest;
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
	
	@PostMapping("/request")
	public ResponseEntity<ApiResponse> requestPasswordReset(@RequestBody UserResetPasswordRequest request) {
	    String email = request.getEmail();

	    if (!userService.isEmailValid(email)) {
	        return ResponseEntity.badRequest().body(new ApiResponse(false, "Email không tồn tại trong hệ thống."));
	    }

	    String otp = userService.generateOtp();

	    long expiryTime = System.currentTimeMillis() + 60 * 1000; 
	    OtpStorage.otpStorage.put(email, new OtpEntry(otp, expiryTime));

	    boolean isSent = userService.sendOtp(email, otp);
	    if (isSent) {
	        return ResponseEntity.ok(new ApiResponse(true, "OTP đã được gửi đến email của bạn."));
	    } else {
	        return ResponseEntity.status(500).body(new ApiResponse(false, "Không thể gửi OTP. Vui lòng thử lại sau."));
	    }
	}


	@PostMapping("/verify")
	public ResponseEntity<ApiResponse> verifyOtpAndResetPassword(
	        @RequestParam String email,
	        @RequestParam String otp,
	        @RequestParam String newPassword) {

	    OtpEntry otpEntry = OtpStorage.otpStorage.get(email);

	    if (otpEntry != null) {
	        long currentTime = System.currentTimeMillis();

	        if (otpEntry.getOtp().equals(otp) && otpEntry.getExpiryTime() > currentTime) {
	            boolean isPasswordReset = userService.resetPassword(email, newPassword);
	            if (isPasswordReset) {
	                OtpStorage.otpStorage.remove(email);
	                return ResponseEntity.ok(new ApiResponse(true, "Mật khẩu đã được đặt lại thành công."));
	            } else {
	                return ResponseEntity.status(500).body(new ApiResponse(false, "Không thể đặt lại mật khẩu. Vui lòng thử lại sau."));
	            }
	        } else if (otpEntry.getExpiryTime() <= currentTime) {
	            OtpStorage.otpStorage.remove(email); 
	            return ResponseEntity.badRequest().body(new ApiResponse(false, "OTP đã hết hạn."));
	        }
	    }

	    return ResponseEntity.badRequest().body(new ApiResponse(false, "OTP không hợp lệ hoặc đã hết hạn."));
	}
}
