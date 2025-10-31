package com.example.turboazapp.domain.model

import com.google.firebase.Timestamp

data class Car(
    val id: String = "",
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val price: Double = 0.0,
    val currency: String = "AZN", // AZN, USD, EUR
    val mileage: Int = 0,
    val city: String = "",
    val fuelType: String = "", // Benzin, Dizel, Elektrik, Hibrid, Qaz
    val transmission: String = "", // Avtomatik, Mexaniki
    val engineVolume: Double = 0.0, // 2.0, 3.5 və s.
    val horsePower: Int = 0, // At gücü
    val color: String = "",
    val bodyType: String = "", // Sedan, Offroad, Hetçbek, Kupe, Universal, Minivan
    val driveType: String = "", // Ön, Arxa, Tam
    val ownerCount: Int = 1, // Neçənci sahibi
    val crashHistory: Boolean = false, // Qəza olub/olmayıb
    val painted: Boolean = false, // Rənglənib/rənglənməyib
    val description: String = "",
    val images: List<String> = emptyList(),
    val sellerId: String = "",
    val sellerName: String = "",
    val phone: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isNew: Boolean = false, // Yeni/İşlənmiş
    val isFavorite: Boolean = false,
    val viewCount: Int = 0,
    val status: CarStatus = CarStatus.ACTIVE
)

enum class CarStatus {
    ACTIVE,      // Aktiv
    SOLD,        // Satılıb
    RESERVED,    // Rezerv edilib
    INACTIVE     // Deaktiv
}
