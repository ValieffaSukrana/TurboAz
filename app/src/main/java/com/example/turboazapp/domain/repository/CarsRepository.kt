package com.example.turboazapp.domain.repository

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.FavoriteCar
import kotlinx.coroutines.flow.Flow

interface CarsRepository {
    // Cars
    suspend fun syncCarsFromApi(): Result<Unit>
    fun getAllCars(): Flow<List<Car>>
    suspend fun getCarsCount(): Int
    suspend fun getCarById(carId: String): Car?

    // Favorites
    suspend fun addToFavorites(car: Car): Result<Unit>
    suspend fun removeFromFavorites(carId: String): Result<Unit>
    fun getFavorites(): Flow<List<FavoriteCar>>
    suspend fun isFavorite(carId: String): Boolean
}