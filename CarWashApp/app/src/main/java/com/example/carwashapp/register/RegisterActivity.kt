package com.example.carwashapp.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.carwashapp.R
import com.example.carwashapp.dashboards.ClientDashboardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val firstNameEt = findViewById<EditText>(R.id.etFirstName)
        val lastNameEt = findViewById<EditText>(R.id.etLastName)
        val emailEt = findViewById<EditText>(R.id.etEmail)
        val passwordEt = findViewById<EditText>(R.id.etPassword)
        val confirmPasswordEt = findViewById<EditText>(R.id.etConfirmPassword)
        val registerBtn = findViewById<Button>(R.id.btnRegister)

        registerBtn.setOnClickListener {
            val firstName = firstNameEt.text.toString().trim()
            val lastName = lastNameEt.text.toString().trim()
            val email = emailEt.text.toString().trim()
            val password = passwordEt.text.toString().trim()
            val confirmPassword = confirmPasswordEt.text.toString().trim()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.passwords_do_not_match), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid
                        val user = hashMapOf(
                            "firstName" to firstName,
                            "lastName" to lastName,
                            "email" to email,
                            "role" to "client"
                        )

                        userId?.let {
                            db.collection("users").document(it)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, ClientDashboardActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, getString(R.string.error_saving_data) + ": ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.registration_failed) + ": ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
