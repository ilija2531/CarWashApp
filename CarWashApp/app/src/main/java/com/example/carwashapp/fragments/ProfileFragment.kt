package com.example.carwashapp.fragments

import BookingAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R
import com.example.carwashapp.model.Booking
import com.example.carwashapp.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBookings)
        val logoutButton = view.findViewById<Button>(R.id.btnLogout)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Преземање на резервации за тековниот корисник
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val bookings = mutableListOf<Booking>()

        FirebaseFirestore.getInstance().collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    val booking = doc.toObject(Booking::class.java)
                    bookings.add(booking)
                }
                recyclerView.adapter = BookingAdapter(bookings)
            }
            .addOnFailureListener {
                context?.let {
                    Toast.makeText(it, "Грешка при читање резервации", Toast.LENGTH_SHORT).show()
                }
            }

        // Logout копче
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
