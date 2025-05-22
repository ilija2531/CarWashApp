package com.example.carwashapp.fragments

import BookingAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R
import com.example.carwashapp.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBookings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
                Toast.makeText(context, "Грешка при читање резервации", Toast.LENGTH_SHORT).show()
            }
    }
}
