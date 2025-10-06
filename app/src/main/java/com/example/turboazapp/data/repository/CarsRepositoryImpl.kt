package com.example.turboazapp.data.repository

import com.example.turboazapp.data.helper.FirebaseHelper
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.model.FavoriteCar
import com.example.turboazapp.domain.repository.CarsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarsRepositoryImpl @Inject constructor(
    private val firebaseHelper: FirebaseHelper
) : CarsRepository {

    override suspend fun syncCarsFromApi(): Result<Unit> {
        // Əgər artıq API-dən maşınları seed etmisinizsə, bu metodu çağırın
        // Dummy data üçün bunu HomeFragment-də istifadə edin
        return Result.success(Unit)
    }

    override fun getAllCars(): Flow<List<Car>> {
        return firebaseHelper.getAllCars()
    }

    override suspend fun getCarsCount(): Int {
        return firebaseHelper.getCarsCount()
    }

    override suspend fun getCarById(carId: String): Car? {
        return firebaseHelper.getCarById(carId)
    }

    override suspend fun addToFavorites(car: Car): Result<Unit> {
        return firebaseHelper.addFavorite(car)
    }

    override suspend fun removeFromFavorites(carId: String): Result<Unit> {
        return firebaseHelper.removeFavorite(carId)
    }

    override fun getFavorites(): Flow<List<FavoriteCar>> {
        return firebaseHelper.getFavorites()
    }

    override suspend fun isFavorite(carId: String): Boolean {
        return firebaseHelper.isFavorite(carId)
    }
}