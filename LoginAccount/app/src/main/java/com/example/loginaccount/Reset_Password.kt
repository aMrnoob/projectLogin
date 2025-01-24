package com.example.loginaccount

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.loginaccount.api.ApiClient
import com.example.loginaccount.api.ApiService
import com.example.loginaccount.model.ApiResponse
import com.example.loginaccount.model.UserResetPasswordRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Reset_Password : ComponentActivity() {

    private var currentStep = 1
    private lateinit var userEmail: String
    private lateinit var otpCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showStep(currentStep)
    }

    private fun showStep(step: Int) {
        when (step) {
            1 -> {
                setContentView(R.layout.reset_password)
                val edtEmail = findViewById<EditText>(R.id.edtEmail)
                val btnContinue = findViewById<Button>(R.id.btnContinue)
                val btnComeback = findViewById<Button>(R.id.btnComeback)

                btnComeback.setOnClickListener {
                    val intent = Intent(this@Reset_Password, MainActivity::class.java)
                    startActivity(intent)
                }

                btnContinue.setOnClickListener {
                    userEmail = edtEmail.text.toString().trim()
                    if (userEmail.isEmpty()) {
                        Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                    } else {
                        requestOtp(UserResetPasswordRequest(userEmail))
                    }
                }
            }
            2 -> {
                setContentView(R.layout.verify_otp)
                val edtOtp = findViewById<EditText>(R.id.edtOtp)
                val btnConfirm = findViewById<Button>(R.id.btnConfirm)
                val btnComeback = findViewById<Button>(R.id.btnComeback)

                btnComeback.setOnClickListener {
                    val intent = Intent(this@Reset_Password, MainActivity::class.java)
                    startActivity(intent)
                }

                btnConfirm.setOnClickListener {
                    val otp = edtOtp.text.toString().trim()
                    if (otp.isEmpty()) {
                        Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_SHORT).show()
                    } else {
                        otpCode = otp
                        verifyOtpAndResetPassword(UserResetPasswordRequest(userEmail,null, otpCode))
                    }
                }
            }
            3 -> {
                setContentView(R.layout.new_password)
                val edtNewPassword = findViewById<EditText>(R.id.edtNewPwd)
                val btnSubmit = findViewById<Button>(R.id.btnConfirm)

                btnSubmit.setOnClickListener {
                    val newPassword = edtNewPassword.text.toString().trim()
                    if (newPassword.isEmpty()) {
                        Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
                    } else {
                        passwordReset(UserResetPasswordRequest(userEmail, newPassword))
                    }
                }
            }
        }
    }

    private fun requestOtp(userResetPasswordRequest: UserResetPasswordRequest) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.requestOtp(userResetPasswordRequest)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    currentStep = 2
                    showStep(currentStep)
                } else {
                    Toast.makeText(this@Reset_Password, response.body()?.message ?: "Failed to send OTP", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Reset_Password, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun verifyOtpAndResetPassword(userResetPasswordRequest:UserResetPasswordRequest) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.verifyOtpAndResetPassword(userResetPasswordRequest)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    currentStep = 3
                    showStep(currentStep)
                } else {
                    Toast.makeText(this@Reset_Password, response.body()?.message ?: "OTP verification failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Reset_Password, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun passwordReset(userResetPasswordRequest:UserResetPasswordRequest) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)
        val call = apiService.passwordReset(userResetPasswordRequest)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    Toast.makeText(this@Reset_Password, "Password reset successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@Reset_Password, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Reset_Password, response.body()?.message ?: "Password reset failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@Reset_Password, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}