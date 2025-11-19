package com.example.turboazapp.domain.usecase

import android.util.Log
import com.example.turboazapp.domain.model.Car
import com.example.turboazapp.domain.repository.AuthRepository
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetFavoriteCarsUseCase @Inject constructor(
    private val carRepository: CarRepository,
    private val authRepository: AuthRepository
) {
    companion object {
        private const val TAG = "GetFavoriteCarsUseCase"
    }

    operator fun invoke(): Flow<Resource<List<Car>>> = flow {
        try {
            Log.d(TAG, "═══════════════════════════════════════")
            Log.d(TAG, "Starting to get favorite cars")
            emit(Resource.Loading())

            // 1. Cari istifadəçini al
            val currentUserResult = authRepository.getCurrentUser()
            Log.d(TAG, "Current user result: ${currentUserResult.javaClass.simpleName}")

            if (currentUserResult is Resource.Error || currentUserResult.data == null) {
                Log.e(TAG, "User not found")
                emit(Resource.Error("İstifadəçi daxil olmayıb"))
                return@flow
            }

            val user = currentUserResult.data
            Log.d(TAG, "User ID: ${user.id}")
            Log.d(TAG, "User favorites: ${user.favorites}")

            // 2. Əgər favorites boşdursa
            if (user.favorites.isEmpty()) {
                Log.d(TAG, "No favorites found")
                emit(Resource.Success(emptyList()))
                return@flow
            }

            // 3. ✅ SADƏ HƏLL: getCars()-dan bütün car-ları gətir və filter et
            Log.d(TAG, "Fetching all cars and filtering favorites...")

            val allCarsResult = carRepository.getCars().firstOrNull { result ->
                // Loading-i skip et, Success və ya Error gözlə
                result !is Resource.Loading
            }

            Log.d(TAG, "All cars result: ${allCarsResult?.javaClass?.simpleName}")

            when (allCarsResult) {
                is Resource.Success -> {
                    val allCars = allCarsResult.data ?: emptyList()
                    Log.d(TAG, "Total cars fetched: ${allCars.size}")

                    // Favorite olan car-ları filter et
                    val favoriteCars = allCars.filter { car ->
                        val isFavorite = user.favorites.contains(car.id)
                        if (isFavorite) {
                            Log.d(TAG, "  ✓ Found favorite: ${car.id} - ${car.brand} ${car.model}")
                        }
                        isFavorite
                    }

                    Log.d(TAG, "Total favorite cars: ${favoriteCars.size}")
                    Log.d(TAG, "═══════════════════════════════════════")

                    emit(Resource.Success(favoriteCars))
                }
                is Resource.Error -> {
                    Log.e(TAG, "Error fetching cars: ${allCarsResult.message}")
                    emit(Resource.Error(allCarsResult.message ?: "Xəta baş verdi"))
                }
                else -> {
                    Log.e(TAG, "Unexpected result: $allCarsResult")
                    emit(Resource.Error("Xəta baş verdi"))
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception in GetFavoriteCarsUseCase", e)
            emit(Resource.Error(e.localizedMessage ?: "Xəta baş verdi"))
        }
    }
}