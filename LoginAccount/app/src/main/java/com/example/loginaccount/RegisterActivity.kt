package com.example.loginaccount

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.loginaccount.api.ApiClient
import com.example.loginaccount.api.ApiService
import com.example.loginaccount.model.ApiResponse
import com.example.loginaccount.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<EditText>(R.id.edtName)
        val email = findViewById<EditText>(R.id.edtEmail)
        val password = findViewById<EditText>(R.id.edtPwd)
        val btnRegister = findViewById<Button>(R.id.btnRegist)
        val btnSignin = findViewById<Button>(R.id.btnSignin)

        btnRegister.setOnClickListener {
            val username = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(username, email, password)
                registerUser(user)
            }
        }

        btnSignin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(user: User) {
        val apiService = ApiClient.getClient().create(ApiService::class.java)

        val call = apiService.register(user)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.isSuccess) {
                        Toast.makeText(this@RegisterActivity, apiResponse.message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@RegisterActivity, apiResponse?.message ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, response.body()?.message ?: "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}