package com.example.carwashapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val date: String,
    val time: String,
    val vehicleType: String,
    val note: String
)
