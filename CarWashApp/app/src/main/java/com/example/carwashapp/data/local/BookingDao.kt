package com.example.carwashapp.data.local

import androidx.room.*

@Dao
interface BookingDao {

    @Query("SELECT * FROM bookings ORDER BY date, time")
    suspend fun getAllBookings(): List<BookingEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<BookingEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingEntity)

    @Query("DELETE FROM bookings")
    suspend fun deleteAll()
}
