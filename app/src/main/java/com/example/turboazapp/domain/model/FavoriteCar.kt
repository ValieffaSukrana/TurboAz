package com.example.turboazapp.domain.model

data class FavoriteCar(
    val id: String = "",
    val carId: String = "",
    val brand: String = "",
    val model: String = "",
    val price: Double = 0.0,
    val url: String = "",
    val engine: String = "",
    val color: String = "",
    val transmission: String = "",
    val fuel: String = "",
    val year: Int = 0,
    val deviceId: String = "",
    val timestamp: Long = 0L
)