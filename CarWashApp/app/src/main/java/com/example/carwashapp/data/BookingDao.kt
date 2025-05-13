package com.example.carwashapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface BookingDao {

    @Insert
    suspend fun insertBooking(booking: BookingEntity)

    @Query("SELECT * FROM bookings ORDER BY timestamp DESC")
    suspend fun getAllBookings(): List<BookingEntity>

    @Query("DELETE FROM bookings")
    suspend fun deleteAllBookings()
}
