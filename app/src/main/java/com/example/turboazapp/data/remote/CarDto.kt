package com.example.turboazapp.data.remote

import com.google.firebase.firestore.PropertyName

data class CarDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("brand") val brand: String = "",
    @PropertyName("model") val model: String = "",
    @PropertyName("year") val year: Int = 0,
    @PropertyName("price") val price: Double = 0.0,
    @PropertyName("currency") val currency: String = "AZN",
    @PropertyName("mileage") val mileage: Int = 0,
    @PropertyName("city") val city: String = "",
    @PropertyName("fuel_type") val fuelType: String = "",
    @PropertyName("transmission") val transmission: String = "",
    @PropertyName("engine_volume") val engineVolume: Double = 0.0,
    @PropertyName("horse_power") val horsePower: Int = 0,
    @PropertyName("color") val color: String = "",
    @PropertyName("body_type") val bodyType: String = "",
    @PropertyName("drive_type") val driveType: String = "",
    @PropertyName("owner_count") val ownerCount: Int = 1,
    @PropertyName("crash_history") val crashHistory: Boolean = false,
    @PropertyName("painted") val painted: Boolean = false,
    @PropertyName("description") val description: String = "",
    @PropertyName("images") val images: List<String> = emptyList(),
    @PropertyName("seller_id") val sellerId: String = "",
    @PropertyName("seller_name") val sellerName: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("created_at") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("updated_at") val updatedAt: Long = System.currentTimeMillis(),
    @PropertyName("is_new") val isNew: Boolean = false,
    @PropertyName("view_count") val viewCount: Int = 0,
    @PropertyName("status") val status: String = "ACTIVE"
)
