package com.example.carwashapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.R
import com.example.carwashapp.dashboards.ClientDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.carwashapp.welcome.WelcomeActivity
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        val emailEt = findViewById<EditText>(R.id.emailEditText)
        val passwordEt = findViewById<EditText>(R.id.passwordEditText)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        loginBtn.setOnClickListener {
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пополнете ги сите полиња", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Успешна најава", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ClientDashboardActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Неуспешна најава: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
