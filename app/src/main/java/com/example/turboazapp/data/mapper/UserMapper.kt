package com.example.turboazapp.data.mapper

import com.example.turboazapp.data.remote.UserDto
import com.example.turboazapp.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        phone = phone,
        profileImage = profileImage,
        favorites = favorites,
        myCars = myCars,
        createdAt = createdAt,
        isVerified = isVerified
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        phone = phone,
        profileImage = profileImage,
        favorites = favorites,
        myCars = myCars,
        createdAt = createdAt,
        isVerified = isVerified
    )
}