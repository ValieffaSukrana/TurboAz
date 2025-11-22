package com.example.turboazapp.domain.repository

import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.Filter
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface CarRepository {
    // Bütün elanları gətir
    fun getCars(): Flow<Resource<List<Car>>>

    // ID-yə görə elan gətir
    fun getCarById(carId: String): Flow<Resource<Car>>

    // Filtrlə axtarış
    fun searchCars(filter: Filter): Flow<Resource<List<Car>>>

    // Yeni elan əlavə et
    suspend fun addCar(car: Car): Resource<String>

    // Elanı yenilə
    suspend fun updateCar(car: Car): Resource<Unit>

    // Elanı sil
    suspend fun deleteCar(carId: String): Resource<Unit>

    // Sevimlilərə əlavə et
    suspend fun addToFavorites(userId: String, carId: String): Resource<Unit>

    // Sevimlilərden sil
    suspend fun removeFromFavorites(userId: String, carId: String): Resource<Unit>

    // Baxış sayını artır
    suspend fun incrementViewCount(carId: String): Resource<Unit>

    fun getCarsByUserId(userId: String): Flow<Resource<List<Car>>>
}