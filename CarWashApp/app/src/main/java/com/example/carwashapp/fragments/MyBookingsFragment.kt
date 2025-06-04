package com.example.carwashapp.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carwashapp.R
import com.example.carwashapp.data.BookingRepository
import com.example.carwashapp.data.local.AppDatabase
import com.example.carwashapp.model.Booking
import com.example.carwashapp.viewmodel.BookingViewModel
import com.example.carwashapp.viewmodel.BookingViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.Calendar

class MyBookingsFragment : Fragment(R.layout.fragment_my_bookings) {

    private lateinit var adapter: BookingAdapter
    private lateinit var viewModel: BookingViewModel
    private lateinit var userId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerBookings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val dao = AppDatabase.getInstance(requireContext()).bookingDao()
        val repository = BookingRepository(dao, FirebaseFirestore.getInstance())
        val factory = BookingViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[BookingViewModel::class.java]

        adapter = BookingAdapter(mutableListOf()) { booking ->
            showOptionsDialog(booking)
        }

        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.loadBookings(userId)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bookings.collect { entities ->
                val bookings = entities.map {
                    Booking(
                        id = it.id,
                        userId = it.userId,
                        date = it.date,
                        time = it.time,
                        vehicleType = it.vehicleType,
                        note = it.note
                    )
                }
                adapter.updateData(bookings)
            }
        }
    }

    private fun showOptionsDialog(booking: Booking) {
        val options = arrayOf(
            getString(R.string.update),
            getString(R.string.delete)
        )
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.select_option))
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showUpdateDialog(booking)
                    1 -> deleteBooking(booking)
                }
            }
            .show()
    }

    private fun showUpdateDialog(booking: Booking) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_update_booking, null)
        val etDate = view.findViewById<EditText>(R.id.etUpdateDate)
        val etTime = view.findViewById<EditText>(R.id.etUpdateTime)

        etDate.setText(booking.date)
        etTime.setText(booking.time)

        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, year, month, day ->
                val dateStr = String.format("%02d/%02d/%04d", day, month + 1, year)
                etDate.setText(dateStr)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val timeStr = String.format("%02d:%02d", hour, minute)
                etTime.setText(timeStr)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.update_booking))
            .setView(view)
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                val newDate = etDate.text.toString()
                val newTime = etTime.text.toString()
                updateBooking(booking, newDate, newTime)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun updateBooking(booking: Booking, newDate: String, newTime: String) {
        booking.date = newDate
        booking.time = newTime

        FirebaseFirestore.getInstance()
            .collection("bookings").document(booking.id!!)
            .update(mapOf("date" to newDate, "time" to newTime))
            .addOnSuccessListener {
                Toast.makeText(context, getString(R.string.updated_successfully), Toast.LENGTH_SHORT).show()
                viewModel.loadBookings(userId)
            }
            .addOnFailureListener {
                Toast.makeText(context, getString(R.string.update_failed), Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteBooking(booking: Booking) {
        FirebaseFirestore.getInstance()
            .collection("bookings").document(booking.id!!)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, getString(R.string.booking_deleted), Toast.LENGTH_SHORT).show()
                viewModel.loadBookings(userId)
            }
            .addOnFailureListener {
                Toast.makeText(context, getString(R.string.delete_failed), Toast.LENGTH_SHORT).show()
            }
    }
}
