package com.example.turboazapp.domain.usecase

import com.example.turboazapp.domain.repository.CarRepository
import com.example.turboazapp.util.Resource
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: CarRepository
) {
    suspend operator fun invoke(
        userId: String,
        carId: String,
        isFavorite: Boolean
    ): Resource<Unit> {
        return if (isFavorite) {
            repository.removeFromFavorites(userId, carId)
        } else {
            repository.addToFavorites(userId, carId)
        }
    }
}