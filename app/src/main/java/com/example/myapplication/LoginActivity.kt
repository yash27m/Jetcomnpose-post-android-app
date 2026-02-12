package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Email
        val emailLayout = findViewById<TextInputLayout>(R.id.emailLayout)
        val emailEditText = findViewById<TextInputEditText>(R.id.emailEditText)

        // Password
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val passwordEditText = findViewById<TextInputEditText>(R.id.passwordEditText)

        // Login Button
        val btnLogin = findViewById<MaterialButton>(R.id.btnlogin)

        btnLogin.setOnClickListener {

            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Clear previous errors
            emailLayout.error = null
            passwordLayout.error = null

            when {
                email.isEmpty() -> {
                    emailLayout.error = "Email is required"
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    emailLayout.error = "Enter a valid email"
                }

                password.isEmpty() -> {
                    passwordLayout.error = "Password is required"
                }

                password.length < 6 -> {
                    passwordLayout.error = "Password must be at least 6 characters"
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Login successfully",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }
}
