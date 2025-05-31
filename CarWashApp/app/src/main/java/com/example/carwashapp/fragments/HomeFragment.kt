package com.example.carwashapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.carwashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcome)
        val btnBookNow = view.findViewById<Button>(R.id.btnBookNow)
        val btnMyBookings = view.findViewById<Button>(R.id.btnMyBookings)
        val tvTip = view.findViewById<TextView>(R.id.tvTip)

        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    val fullName = document.getString("fullName") ?: ""
                    tvWelcome.text = "Добредојдовте, $fullName!"
                }
        }



        btnBookNow.setOnClickListener {
            findNavController().navigate(R.id.bookFragment)
        }

        btnMyBookings.setOnClickListener {
            findNavController().navigate(R.id.myBookingsFragment)
        }


        // Совет на денот (може да ротираш)
        val tips = listOf(
            "Избегнувајте миење на автомобилот на директно сонце.",
            "Редовно чистете ја внатрешноста за подобра свежина.",
            "Користете микрофибер крпа за сушење.",
            "Проверувајте гумите по секое миење.",
            "Не заборавајте на миење на тркалата!"
        )
        val randomTip = tips.random()
        tvTip.text = "🚗 Совет на денот:\n„$randomTip“"
    }
}
