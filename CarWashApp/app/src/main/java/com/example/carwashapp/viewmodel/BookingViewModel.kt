package com.example.carwashapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carwashapp.data.BookingRepository
import com.example.carwashapp.data.local.BookingEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {
    private val _bookings = MutableStateFlow<List<BookingEntity>>(emptyList())
    val bookings: StateFlow<List<BookingEntity>> = _bookings

    fun loadBookings(userId: String) {
        viewModelScope.launch {
            repository.syncBookingsFromFirestore(userId)
            _bookings.value = repository.getLocalBookings()
        }
    }
}
