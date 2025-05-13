package com.example.carwashapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val date: String,
    val time: String,
    val vehicleType: String,
    val note: String,
    val timestamp: Long
)
