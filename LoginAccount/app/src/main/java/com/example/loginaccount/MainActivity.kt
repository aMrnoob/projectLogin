package com.example.loginaccount

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.loginaccount.RegisterActivity
import com.example.loginaccount.api.ApiClient
import com.example.loginaccount.api.ApiService
import com.example.loginaccount.model.ApiResponse
import com.example.loginaccount.model.User
import com.example.loginaccount.model.UserLoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val edtUserName = findViewById<EditText>(R.id.edtUserName)
        val edtPassword = findViewById<EditText>(R.id.edtPwd)

        btnLogin.setOnClickListener {
            val username = edtUserName.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                val userLoginRequest = UserLoginRequest(username, password)
                login(userLoginRequest)
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(userLoginRequest:UserLoginRequest) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.login(userLoginRequest)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.isSuccess) {
                        Toast.makeText(this@MainActivity, "Login successful", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@MainActivity, apiResponse?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Login failed: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
