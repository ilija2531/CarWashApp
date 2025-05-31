package com.example.carwashapp.fragments

import BookingAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R
import com.example.carwashapp.model.Booking
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {

    private val db = FirebaseFirestore.getInstance()
    private val bookings = mutableListOf<Booking>()
    private lateinit var adapter: BookingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBookings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        db.collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                bookings.clear()
                for (doc in result) {
                    val booking = doc.toObject(Booking::class.java)
                    booking.id = doc.id // важно за бришење/ажурирање
                    bookings.add(booking)
                }

                adapter = BookingAdapter(bookings) { booking ->
                    showOptionsDialog(booking)
                }

                recyclerView.adapter = adapter
            }
            .addOnFailureListener {
                Toast.makeText(context, "Грешка при читање резервации", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showOptionsDialog(booking: Booking) {
        val options = arrayOf("Ажурирај", "Избриши")
        AlertDialog.Builder(requireContext())
            .setTitle("Избери опција")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showUpdateDialog(booking)
                    1 -> deleteBooking(booking)
                }
            }
            .show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showUpdateDialog(booking: Booking) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_update_booking, null)
        val etDate = view.findViewById<EditText>(R.id.etUpdateDate)
        val etTime = view.findViewById<EditText>(R.id.etUpdateTime)

        etDate.setText(booking.date)
        etTime.setText(booking.time)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(requireContext(), { _, year, month, day ->
                val dateString = String.format("%02d/%02d/%04d", day, month + 1, year)
                etDate.setText(dateString)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(requireContext(), { _, hour, minute ->
                val timeString = String.format("%02d:%02d", hour, minute)
                etTime.setText(timeString)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePicker.show()
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Ажурирај резервација")
            .setView(view)
            .setPositiveButton("Зачувај") { _, _ ->
                val newDate = etDate.text.toString()
                val newTime = etTime.text.toString()
                updateBooking(booking, newDate, newTime)
            }
            .setNegativeButton("Откажи", null)
            .show()
    }

    private fun updateBooking(booking: Booking, newDate: String, newTime: String) {
        db.collection("bookings").document(booking.id!!)
            .update(mapOf(
                "date" to newDate,
                "time" to newTime
            ))
            .addOnSuccessListener {
                Toast.makeText(context, "Успешно ажурирано", Toast.LENGTH_SHORT).show()
                booking.date = newDate
                booking.time = newTime
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Грешка при ажурирање", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteBooking(booking: Booking) {
        db.collection("bookings").document(booking.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Резервацијата е избришана", Toast.LENGTH_SHORT).show()
                bookings.remove(booking)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Грешка при бришење", Toast.LENGTH_SHORT).show()
            }
    }
}
