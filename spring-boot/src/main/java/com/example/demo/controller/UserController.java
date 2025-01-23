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
	
	@PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody UserResetPasswordRequest request) {
        String email = request.getEmail();

        if (!userService.isEmailValid(email)) {
            return ResponseEntity.badRequest().body("Email không tồn tại trong hệ thống.");
        }

        String otp = userService.generateOtp();

        boolean isSent = userService.sendOtp(email, otp);

        if (isSent) {
            otpStorage.put(email, otp);
            return ResponseEntity.ok("OTP đã được gửi đến email của bạn.");
        } else {
            return ResponseEntity.status(500).body("Không thể gửi OTP. Vui lòng thử lại sau.");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtpAndResetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            boolean isPasswordReset = userService.resetPassword(email, newPassword);
            if (isPasswordReset) {
                otpStorage.remove(email);
                return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
            } else {
                return ResponseEntity.status(500).body("Không thể đặt lại mật khẩu. Vui lòng thử lại sau.");
            }
        } else {
            return ResponseEntity.badRequest().body("OTP không hợp lệ hoặc đã hết hạn.");
        }
    }
}
