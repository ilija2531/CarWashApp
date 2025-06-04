package com.example.carwashapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.carwashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var tvWelcome: TextView
    private lateinit var tvTip: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        tvWelcome = view.findViewById(R.id.tvWelcome)
        tvTip = view.findViewById(R.id.tvTip)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        loadUserFullName()
        setLocalizedCarTip()

        return view
    }

    private fun loadUserFullName() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    tvWelcome.text = getString(R.string.welcome_user, "$firstName $lastName")
                }
            }
            .addOnFailureListener {
                tvWelcome.text = getString(R.string.welcome)
            }
    }

    private fun setLocalizedCarTip() {
        val tips = listOf(
            getString(R.string.tip_1),
            getString(R.string.tip_2),
            getString(R.string.tip_3)
        )
        tvTip.text = getString(R.string.car_tip_label, tips.random())
    }
}
