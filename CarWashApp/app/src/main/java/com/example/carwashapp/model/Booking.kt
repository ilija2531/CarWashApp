package com.example.carwashapp.model

data class Booking(
    var id: String? = null,
    val userId: String = "",
    var date: String = "",
    var time: String = "",
    val vehicleType: String = "",
    val note: String = ""
)
