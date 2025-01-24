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
	        return ResponseEntity.ok(new ApiResponse(true, "Đăng ký tài khoản thành công"));
	    } else {
	        return ResponseEntity.badRequest().body(new ApiResponse(false, "Tên đăng nhập hoặc email không tồn tại"));
	    }
	}

	@PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        Optional<User> validUser = userService.login(userLoginRequest);
        if (validUser.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(true, "Đăng nhập thành công"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Tên đăng nhập hoặc mật khẩu bị sai"));
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
	        return ResponseEntity.ok(new ApiResponse(true, "OTP đã được gửi đến email của bạn.", OtpStorage.otpStorage));
	    } else {
	        return ResponseEntity.status(500).body(new ApiResponse(false, "Không thể gửi OTP. Vui lòng thử lại sau."));
	    }
	}


	@PostMapping("/verify")
	public ResponseEntity<ApiResponse> verifyOtpAndResetPassword(@RequestBody UserResetPasswordRequest request) {
		
		String email = request.getEmail();
		String otp = request.getOtp();
		
		OtpEntry otpEntry = OtpStorage.otpStorage.get(email);
		
	    if (otpEntry != null) {
	        long currentTime = System.currentTimeMillis();

	        if (otpEntry.getOtp().equals(otp) && otpEntry.getExpiryTime() > currentTime) {
	            return ResponseEntity.ok(new ApiResponse(true, "OTP hợp lệ. Bạn có thể đổi mật khẩu mới."));
	        } else if (otpEntry.getExpiryTime() <= currentTime) {
	            OtpStorage.otpStorage.remove(email); 
	            return ResponseEntity.badRequest().body(new ApiResponse(false, "OTP đã hết hạn."));
	        }
	    }

	    return ResponseEntity.badRequest().body(new ApiResponse(false, "OTP không hợp lệ."));
	}
	
	@PostMapping("/reset")
	public ResponseEntity<ApiResponse> passwordReset(@RequestBody UserResetPasswordRequest request) {
	    String email = request.getEmail();
	    String newPassword = request.getNewPassword();

	    boolean isReset = userService.resetPassword(email, newPassword);
	    if (isReset) {
	        return ResponseEntity.ok(new ApiResponse(true, "Thay đổi mật khẩu thành công."));
	    } else {
	        return ResponseEntity.status(500).body(new ApiResponse(false, "Thay đổi mật khẩu thất bại. Vui lòng thử lại."));
	    }
	}
}
