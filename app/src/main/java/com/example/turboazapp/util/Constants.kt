package com.example.turboazapp.util

object Constants {
    // Firebase Collections
    const val CARS_COLLECTION = "cars"
    const val USERS_COLLECTION = "users"

    // Brands
    val CAR_BRANDS = listOf(
        "BMW", "Mercedes-Benz", "Toyota", "Honda", "Hyundai",
        "Kia", "Nissan", "Volkswagen", "Audi", "Lexus",
        "Mazda", "Chevrolet", "Ford", "Opel", "Renault"
    )

    // Cities
    val CITIES = listOf(
        "Bakı", "Gəncə", "Sumqayıt", "Mingəçevir",
        "Şəki", "Lənkəran", "Quba", "Şamaxı"
    )

    // Fuel Types
    val FUEL_TYPES = listOf("Benzin", "Dizel", "Qaz", "Elektrik", "Hibrid")

    // Transmission
    val TRANSMISSIONS = listOf("Avtomatik", "Mexaniki")
}