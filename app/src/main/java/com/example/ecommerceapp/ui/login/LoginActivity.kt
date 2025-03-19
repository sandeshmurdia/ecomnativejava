package com.example.ecommerceapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerceapp.databinding.ActivityLoginBinding
import com.example.ecommerceapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import com.zipy.zipyandroid.Zipy

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Log successful login with user details
                val loginDetails = mapOf(
                    "email" to email,
                    "timestamp" to System.currentTimeMillis(),
                    "device_type" to "Android",
                    "login_method" to "email",
                    "success" to true
                )
                Zipy.logMessage("User logged in successfully", loginDetails)
                
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Log login error with validation details
                val errorDetails = mapOf(
                    "email" to email,
                    "email_empty" to email.isEmpty(),
                    "password_empty" to password.isEmpty(),
                    "timestamp" to System.currentTimeMillis(),
                    "error_type" to "validation_error",
                    "device_type" to "Android"
                )
                Zipy.logError("Login validation failed", errorDetails)
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            // Log forgot password with context
            val forgotPasswordDetails = mapOf(
                "timestamp" to System.currentTimeMillis(),
                "screen" to "login",
                "device_type" to "Android",
                "email" to binding.etEmail.text.toString()
            )
            Zipy.logMessage("User clicked forgot password", forgotPasswordDetails)
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }
    }
} 