package com.example.carwashapp.data

import com.example.carwashapp.data.local.BookingDao
import com.example.carwashapp.data.local.BookingEntity
import com.example.carwashapp.model.Booking
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BookingRepository(
    private val bookingDao: BookingDao,
    private val firestore: FirebaseFirestore
) {

    suspend fun syncBookingsFromFirestore(userId: String) {
        val snapshot = firestore.collection("bookings")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        val bookings = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Booking::class.java)?.copy(id = doc.id)
        }

        val bookingEntities = bookings.map {
            BookingEntity(
                id = it.id ?: return@map null,
                userId = it.userId,
                date = it.date,
                time = it.time,
                vehicleType = it.vehicleType,
                note = it.note
            )
        }.filterNotNull()

        bookingDao.deleteAll()
        bookingDao.insertBookings(bookingEntities)
    }

    suspend fun getLocalBookings(): List<BookingEntity> = bookingDao.getAllBookings()
}
