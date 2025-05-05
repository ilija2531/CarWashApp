package com.example.carwashapp.register

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.databinding.ActivityRegisterBinding
import com.example.carwashapp.dashboards.ClientDashboardActivity
import com.example.carwashapp.dashboards.EmployeeDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var selectedRole: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Spinner за улоги


        // Register Button
        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedRole.isEmpty()) {
                Toast.makeText(this, "Пополнете ги сите полиња!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Лозинките не се совпаѓаат!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser!!.uid
                    val user = hashMapOf(
                        "email" to email,
                        "role" to selectedRole
                    )
                    Firebase.firestore.collection("users").document(userId).set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Успешна регистрација!", Toast.LENGTH_SHORT).show()
                            val intent = when (selectedRole) {
                                "Клиент" -> Intent(this, ClientDashboardActivity::class.java)
                                "Вработен" -> Intent(this, EmployeeDashboardActivity::class.java)
                                else -> null
                            }
                            startActivity(intent)
                            finish()
                        }
                } else {
                    Toast.makeText(this, "Регистрацијата не успеа: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
