package com.example.turboazapp.data.remote

import com.google.firebase.firestore.PropertyName

data class UserDto(
    @PropertyName("id") val id: String = "",
    @PropertyName("name") val name: String = "",
    @PropertyName("phone") val phone: String = "",
    @PropertyName("profile_image") val profileImage: String = "",
    @PropertyName("favorites") val favorites: List<String> = emptyList(),
    @PropertyName("my_cars") val myCars: List<String> = emptyList(),
    @PropertyName("created_at") val createdAt: Long = System.currentTimeMillis(),
    @PropertyName("is_verified") val isVerified: Boolean = false
)
