package com.example.carwashapp.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.carwashapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import com.example.carwashapp.model.Booking

class BookFragment : Fragment(R.layout.fragment_book) {

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dateButton = view.findViewById<Button>(R.id.btnPickDate)
        val timeButton = view.findViewById<Button>(R.id.btnPickTime)
        val submitButton = view.findViewById<Button>(R.id.btnSubmitBooking)
        val tvDate = view.findViewById<TextView>(R.id.tvSelectedDate)
        val tvTime = view.findViewById<TextView>(R.id.tvSelectedTime)
        val spinner = view.findViewById<Spinner>(R.id.spinnerVehicleType)
        val etNote = view.findViewById<EditText>(R.id.etNote)

        // Spinner items
        val vehicleTypes = arrayOf("Мал автомобил", "Седан", "Комби", "Џип")
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, vehicleTypes)

        val calendar = Calendar.getInstance()

        dateButton.setOnClickListener {
            val datePicker = DatePickerDialog(requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(calendar.time)
                    tvDate.text = selectedDate
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }

        timeButton.setOnClickListener {
            val timePicker = TimePickerDialog(requireContext(),
                { _, hour, minute ->
                    selectedTime = String.format("%02d:%02d", hour, minute)
                    tvTime.text = selectedTime
                },
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
            timePicker.show()
        }

        submitButton.setOnClickListener {
            val vehicleType = spinner.selectedItem.toString()
            val note = etNote.text.toString()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setOnClickListener

            if (!::selectedDate.isInitialized || !::selectedTime.isInitialized) {
                Toast.makeText(requireContext(), "Изберете датум и време", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val booking = hashMapOf(
                "userId" to userId,
                "date" to selectedDate,
                "time" to selectedTime,
                "vehicleType" to vehicleType,
                "note" to note,
                "timestamp" to System.currentTimeMillis()
            )

            FirebaseFirestore.getInstance().collection("bookings")
                .add(booking)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Резервацијата е успешно снимена", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Грешка при резервација: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }

        }
    }
}
