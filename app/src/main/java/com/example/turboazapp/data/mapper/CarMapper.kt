package com.example.turboazapp.data.mapper

import com.example.turboazapp.data.remote.CarDto
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.CarStatus

fun CarDto.toDomain(): Car {
    return Car(
        id = id,
        brand = brand,
        model = model,
        year = year,
        price = price,
        currency = currency,
        mileage = mileage,
        city = city,
        fuelType = fuelType,
        transmission = transmission,
        engineVolume = engineVolume,
        horsePower = horsePower,
        color = color,
        bodyType = bodyType,
        driveType = driveType,
        ownerCount = ownerCount,
        crashHistory = crashHistory,
        painted = painted,
        description = description,
        images = images,
        sellerId = sellerId,
        sellerName = sellerName,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isNew = isNew,
        viewCount = viewCount,
        status = when (status) {
            "ACTIVE" -> CarStatus.ACTIVE
            "SOLD" -> CarStatus.SOLD
            "RESERVED" -> CarStatus.RESERVED
            "INACTIVE" -> CarStatus.INACTIVE
            else -> CarStatus.ACTIVE
        },
        isFavorite = false // Bu UI-da set olunacaq
    )
}

fun Car.toDto(): CarDto {
    return CarDto(
        id = id,
        brand = brand,
        model = model,
        year = year,
        price = price,
        currency = currency,
        mileage = mileage,
        city = city,
        fuelType = fuelType,
        transmission = transmission,
        engineVolume = engineVolume,
        horsePower = horsePower,
        color = color,
        bodyType = bodyType,
        driveType = driveType,
        ownerCount = ownerCount,
        crashHistory = crashHistory,
        painted = painted,
        description = description,
        images = images,
        sellerId = sellerId,
        sellerName = sellerName,
        phone = phone,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isNew = isNew,
        viewCount = viewCount,
        status = status.name
    )
}