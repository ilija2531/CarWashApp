package com.example.carwashapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R

import com.example.carwashapp.data.BookingDatabase
import com.example.carwashapp.data.BookingEntity
import com.example.carwashapp.model.Booking
import com.example.carwashapp.welcome.WelcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.res.Configuration
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.AdapterView
import java.util.Locale

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBookings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val bookings = mutableListOf<Booking>()

        val spinner = view.findViewById<Spinner>(R.id.spinnerLanguage)
        val options = listOf("Македонски", "English", "Одјави се")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        // Sync with Firestore and store in Room
        FirebaseFirestore.getInstance().collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val dao = BookingDatabase.getDatabase(requireContext()).bookingDao()
                    dao.deleteAllBookings()

                    for (doc in result) {
                        val booking = doc.toObject(Booking::class.java)
                        bookings.add(booking)
                        val entity = BookingEntity(
                            userId = userId,
                            date = booking.date,
                            time = booking.time,
                            vehicleType = booking.vehicleType,
                            note = booking.note,
                            timestamp = System.currentTimeMillis()
                        )
                        dao.insertBooking(entity)
                    }

                    withContext(Dispatchers.Main) {
                        recyclerView.adapter = BookingAdapter(bookings)
                    }
                }
            }
            .addOnFailureListener {
                context?.let {
                    Toast.makeText(it, "Грешка при читање резервации", Toast.LENGTH_SHORT).show()
                }
            }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLocale("mk")
                    1 -> setLocale("en")
                    2 -> {
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(requireContext(), WelcomeActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }
    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        requireActivity().recreate()
    }

}
