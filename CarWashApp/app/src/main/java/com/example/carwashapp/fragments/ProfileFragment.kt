package com.example.carwashapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.carwashapp.R
import com.example.carwashapp.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var logoutButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameTextView = view.findViewById(R.id.txtFullName)
        emailTextView = view.findViewById(R.id.txtEmail)
        logoutButton = view.findViewById(R.id.btnLogout)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email ?: getString(R.string.unknown)
            emailTextView.text = getString(R.string.email_format, userEmail)
            loadUserFullName(currentUser.uid)
        } else {
            emailTextView.text = getString(R.string.email_format, getString(R.string.unknown))
            nameTextView.text = getString(R.string.name, getString(R.string.unknown))
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun loadUserFullName(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val firstName = document.getString("firstName") ?: getString(R.string.unknown)
                    val lastName = document.getString("lastName") ?: ""
                    nameTextView.text = getString(R.string.name, "$firstName $lastName")
                } else {
                    nameTextView.text = getString(R.string.name, getString(R.string.unknown))
                }
            }
            .addOnFailureListener {
                nameTextView.text = getString(R.string.name, getString(R.string.load_error))
            }
    }
}
