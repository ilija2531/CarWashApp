package com.example.carwashapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.carwashapp.data.BookingRepository

class BookingViewModelFactory(private val repository: BookingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
