package com.example.turboazapp.domain.model

import com.google.firebase.Timestamp

data class Car(
    val price: Double=0.0,
    val id: String="",
    val brand: String="",
    val model: String="",
    val url: String = "",
    val engine: String="",
    val color: String="",
    val transmission: String="",
    val fuel: String="",
    val year: Int=0
)
