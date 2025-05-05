package com.example.carwashapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.databinding.ActivityLoginBinding
import com.example.carwashapp.dashboards.EmployeeDashboardActivity
import com.example.carwashapp.dashboards.ClientDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        val uid = auth.currentUser?.uid
                        if (uid != null) {
                            Firebase.firestore.collection("users").document(uid)
                                .get()
                                .addOnSuccessListener { document ->
                                    val role = document.getString("role")
                                    when (role) {
                                        "employee" -> startActivity(Intent(this, EmployeeDashboardActivity::class.java))
                                        "client" -> startActivity(Intent(this, ClientDashboardActivity::class.java))
                                        else -> Toast.makeText(this, "Улогата не е препознаена", Toast.LENGTH_SHORT).show()
                                    }
                                    finish()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Најавата не успеа: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Внесете емаил и лозинка", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
