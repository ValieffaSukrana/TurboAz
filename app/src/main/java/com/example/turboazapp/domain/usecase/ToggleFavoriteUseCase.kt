package com.example.turboazapp.domain.usecase

import android.util.Log
import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CarRepository
) {
    companion object {
        private const val TAG = "ToggleFavoriteUseCase"
    }

    suspend operator fun invoke(
        userId: String,
        carId: String,
        isFavorite: Boolean
    ): Resource<Unit> {
        Log.d(TAG, "Toggle called: userId=$userId, carId=$carId, currentlyFavorite=$isFavorite")

        return if (isFavorite) {
            Log.d(TAG, "Removing from favorites...")
            val result = repository.removeFromFavorites(userId, carId)
            Log.d(TAG, "Remove result: $result")
            result
        } else {
            Log.d(TAG, "Adding to favorites...")
            val result = repository.addToFavorites(userId, carId)
            Log.d(TAG, "Add result: $result")
            result
        }
    }
}