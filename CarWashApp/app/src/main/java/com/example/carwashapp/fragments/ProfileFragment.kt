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

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val logoutButton = view.findViewById<Button>(R.id.btnLogout)
        val emailTextView = view.findViewById<TextView>(R.id.txtEmail)
        val nameTextView = view.findViewById<TextView>(R.id.txtFullName)

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: "Непознат"
        val fullName = user?.displayName ?: "Непознат"

        emailTextView.text = "Email: $email"
        nameTextView.text = "Име и презиме: $fullName"

        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
