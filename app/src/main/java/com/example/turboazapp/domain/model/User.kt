package com.example.turboazapp.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val profileImage: String = "",
    val favorites: List<String> = emptyList(),
    val myCars: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isVerified: Boolean = false
)
